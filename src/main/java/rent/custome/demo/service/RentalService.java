package rent.custome.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rent.custome.demo.entity.*;
import rent.custome.demo.enums.HinhThucThue;
import rent.custome.demo.enums.PhieuThueStatus;
import rent.custome.demo.enums.TrangPhucStatus;
import rent.custome.demo.enums.TrangThaiDatCoc;
import rent.custome.demo.repository.*;

@Service
public class RentalService {

    private static final Logger log = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository repository;
    private final CartRepository gioHangRepository;
    private final CartItemRepository chiTietGioHangRepository;
    private final CustomeRepository trangPhucRepository;

    public RentalService(RentalRepository repository,
                            CartRepository gioHangRepository,
                            CartItemRepository chiTietGioHangRepository,
                            CustomeRepository trangPhucRepository) {
        this.repository = repository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.trangPhucRepository = trangPhucRepository;
    }

    public Optional<Rental> findById(Long id) {
        return repository.findById(id);
    }

    public List<Rental> findByKhachHangId(Long khachHangId) {
        return repository.findByKhachHangId(khachHangId);
    }

    @Transactional
    public Rental createFromCart(Long khachHangId, Rental req) {
        log.info("Tao phieu thue cho khachHangId={}", khachHangId);

        if (!req.getNgayHenTra().isAfter(req.getNgayHenLay())) {
            throw new RuntimeException("Ngày hẹn trả phải sau ngày hẹn lấy");
        }

        Cart cart = gioHangRepository.findByKhachHangId(khachHangId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống, hãy thêm trang phục trước"));

        List<CartItem> cartItems = chiTietGioHangRepository.findByGioHangId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, hãy thêm trang phục trước");
        }

        Rental phieuThue = new Rental();
        phieuThue.setKhachHangId(khachHangId);
        phieuThue.setNgayTao(LocalDate.now());
        phieuThue.setNgayHenLay(req.getNgayHenLay());
        phieuThue.setNgayHenTra(req.getNgayHenTra());
        phieuThue.setHinhThuc(HinhThucThue.ONLINE);
        phieuThue.setTrangThai(PhieuThueStatus.CHO_DAT_COC);
        phieuThue.setTienDatCoc(0.0);
        phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.CHUA_THANH_TOAN);
        phieuThue.setChiTiet(new ArrayList<>());

        int cnt = 0;
        for (CartItem item : cartItems) {
            Custome tp = trangPhucRepository.findById(item.getTrangPhucId()).orElse(null);
            if (tp != null && tp.getSoLuong() > 0 && tp.getTrangThai() == TrangPhucStatus.SAN_HANG) {
                phieuThue.getChiTiet().add(
                    new RentalItem(phieuThue.getId(), tp.getId(),
                                        item.getSoLuong(), item.getSoLuong() * tp.getGiaThue()));
                int conLai = tp.getSoLuong() - item.getSoLuong();
                tp.setSoLuong(conLai);
                if (conLai == 0) tp.setTrangThai(TrangPhucStatus.KHONG_CON_HANG);
                trangPhucRepository.save(tp);
                cnt++;
            }
        }

        if (cnt == 0) throw new RuntimeException("Tất cả trang phục trong giỏ đã hết hàng hoặc không còn");

        double tienCoc = phieuThue.getChiTiet().stream()
                .mapToDouble(ct -> ct.getDonGia() != null ? ct.getDonGia() : 0).sum() * 0.3;
        phieuThue.setTienDatCoc(tienCoc);

        Rental saved = repository.save(phieuThue);
        chiTietGioHangRepository.deleteAll(cartItems);
        cart.setNgayCapNhat(LocalDate.now());
        gioHangRepository.save(cart);

        log.info("Tao phieu thue thanh cong id={}", saved.getId());
        return saved;
    }

    @Transactional
    public void datCoc(Long phieuThueId) {
        Rental phieuThue = findOrThrow(phieuThueId);

        if (phieuThue.getTrangThai() == PhieuThueStatus.DA_HUY) {
            throw new IllegalStateException("Phiếu đã bị hủy, không thể đặt cọc");
        }
        if (phieuThue.getTrangThai() == PhieuThueStatus.CHO_XAC_NHAN) {
            throw new IllegalStateException("Phiếu đã được đặt cọc trước đó");
        }

        phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.DA_THANH_TOAN);
        phieuThue.setTrangThai(PhieuThueStatus.CHO_XAC_NHAN);

        repository.save(phieuThue);
        log.info("Dat coc phieu {} thanh cong", phieuThueId);
    }

    @Transactional
    public void huyPhieu(Long phieuThueId) {
        Rental phieuThue = findOrThrow(phieuThueId);
       
        if (phieuThue.getTrangThai() == PhieuThueStatus.DA_HUY) {
            throw new IllegalStateException("Phiếu đã bị hủy trước đó");
        }

        if (phieuThue.getTrangThai() == PhieuThueStatus.CHO_XAC_NHAN) {
            phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.DA_HOAN_TRA);
        }

        phieuThue.setTrangThai(PhieuThueStatus.DA_HUY);

        // Hoàn tồn kho khi hủy
        for (RentalItem ct : phieuThue.getChiTiet()) {
            trangPhucRepository.findById(ct.getTrangPhucId()).ifPresent(tp -> {
                tp.setSoLuong(tp.getSoLuong() + ct.getSoLuong());
                if (tp.getTrangThai() == TrangPhucStatus.KHONG_CON_HANG)
                    tp.setTrangThai(TrangPhucStatus.SAN_HANG);
                trangPhucRepository.save(tp);
            });
        }

        repository.save(phieuThue);
        log.info("Huy phieu {} thanh cong", phieuThueId);
    }

    private Rental findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu thuê id=" + id));
    }
}
