package rent.custome.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rent.custome.demo.dto.CreateOrderRequest;
import rent.custome.demo.entity.ChiTietGioHang;
import rent.custome.demo.entity.ChiTietPhieuThue;
import rent.custome.demo.entity.GioHang;
import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.enums.HinhThucThue;
import rent.custome.demo.enums.PhieuThueStatus;
import rent.custome.demo.enums.TrangPhucStatus;
import rent.custome.demo.enums.TrangThaiDatCoc;
import rent.custome.demo.repository.ChiTietGioHangRepository;
import rent.custome.demo.repository.GioHangRepository;
import rent.custome.demo.repository.PhieuThueRepository;
import rent.custome.demo.repository.TrangPhucRepository;

@Service
public class PhieuThueService {
    private static final Logger log = LoggerFactory.getLogger(PhieuThueService.class);

    private final PhieuThueRepository repository;
    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final TrangPhucRepository trangPhucRepository;

    

    public PhieuThueService(PhieuThueRepository repository, GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository, TrangPhucRepository trangPhucRepository) {
        this.repository = repository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.trangPhucRepository = trangPhucRepository;
    }

    public Optional<PhieuThue> findById(Long phieuThueId){
        return repository.findById(phieuThueId);
    }


    @Transactional
    public PhieuThue createFromCart(Long khachHangId, CreateOrderRequest req){
        log.info("Yeu cau tao phieu thue cho khach hang khachHangId={}", khachHangId);

        if (!req.getNgayHenTra().isAfter(req.getNgayHenLay())) {
            throw new RuntimeException("Ngày hẹn trả phải sau ngày hẹn lấy");
        }

        GioHang cart = gioHangRepository.findByKhachHangId(khachHangId)
                                        .orElseThrow(() -> new RuntimeException("Giỏ hàng trống, hãy thêm trang phục trước"));

        List<ChiTietGioHang> cartItems = chiTietGioHangRepository.findByGioHangId(cart.getId());
        if(cartItems.isEmpty()){
            throw new RuntimeException("Giỏ hàng trống, hãy thêm trang phục trước");
        }
        
        PhieuThue phieuThue = new PhieuThue();
        phieuThue.setKhachHangId(khachHangId);
        phieuThue.setNgayTao(LocalDate.now());
        phieuThue.setNgayHenLay(req.getNgayHenLay());
        phieuThue.setNgayHenTra(req.getNgayHenTra());
        phieuThue.setHinhThuc(HinhThucThue.ONLINE);
        phieuThue.setTrangThai(PhieuThueStatus.CHO_XU_LY);
        phieuThue.setTienDatCoc(0.0);
        phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.CHUA_THANH_TOAN);
        phieuThue.setChiTiet(new ArrayList<>());

        int cnt = 0;
        for(ChiTietGioHang cartItem : cartItems){
            TrangPhuc trangPhuc = trangPhucRepository.findById(cartItem.getTrangPhucId()).orElse(null);

            if(trangPhuc != null && trangPhuc.getSoLuong() > 0 && trangPhuc.getTrangThai() == TrangPhucStatus.SAN_HANG){
                phieuThue.getChiTiet().add(
                    new ChiTietPhieuThue(phieuThue.getId(), 
                                        trangPhuc.getId(),
                                        cartItem.getSoLuong(),
                                        cartItem.getSoLuong()*trangPhuc.getGiaThue()));
                    int instock = trangPhuc.getSoLuong()-cartItem.getSoLuong();
                    trangPhuc.setSoLuong(instock);
                    if(instock == 0) trangPhuc.setTrangThai(TrangPhucStatus.KHONG_CON_HANG);
                    trangPhucRepository.save(trangPhuc);
                    cnt++;
            }
        }

        if(cnt ==0){
            throw new RuntimeException("Tất cả trang phục trong giỏ đã bị người khác thuê mất");
        }

        double tienDatCoc = phieuThue.getChiTiet().stream()
                            .mapToDouble(ct -> ct.getDonGia() != null ? ct.getDonGia() : 0).sum() * 0.3;
        
        phieuThue.setTienDatCoc(tienDatCoc);
        PhieuThue saved = repository.save(phieuThue);
        chiTietGioHangRepository.deleteAll(cartItems);

        cart.setNgayCapNhat(LocalDate.now());
        gioHangRepository.save(cart);

        log.info("Tao phieu thue thanh cong: id={}", saved.getId());

        return saved;
    }

    public void datCoc(Long phieuThueId){
        PhieuThue phieuThue = repository.findById(phieuThueId).orElse(null);

        if(phieuThue == null){
            throw new RuntimeException("Phiếu thuê trang phục không tồn tại");
        }

        phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.DA_THANH_TOAN);
        repository.save(phieuThue);

        log.info("Da dat coc tien thue thanh cong");
    }

    public void huyPhieu(Long phieuThueId){
        PhieuThue phieuThue = repository.findById(phieuThueId).orElse(null);
        if(phieuThue == null){
            throw new RuntimeException("Phiêu thuê trang phục không tồn tại");
        }

        phieuThue.setTrangThai(PhieuThueStatus.DA_HUY);
        if(phieuThue.getTrangThaiDatCoc() == TrangThaiDatCoc.DA_THANH_TOAN){
            phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.DA_HOAN_TRA);
        }
        
        repository.save(phieuThue);

        log.info("Da huy phieu thue " + phieuThueId + "thanh cong");
    }

    public List<PhieuThue> findByKhachHangId(Long khachHangId){
        List<PhieuThue> phieus = repository.findByKhachHangId(khachHangId);

        log.info("Lay thanh cong tat ca phieu thue cua khach hang id={}", khachHangId);

        return phieus;
    }
}
