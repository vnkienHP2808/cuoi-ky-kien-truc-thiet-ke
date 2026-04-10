package rent.custome.demo.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rent.custome.demo.entity.ChiTietGioHang;
import rent.custome.demo.entity.GioHang;
import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.repository.ChiTietGioHangRepository;
import rent.custome.demo.repository.GioHangRepository;
import rent.custome.demo.repository.TrangPhucRepository;

@Service
public class GioHangService {
    private static final Logger log = LoggerFactory.getLogger(GioHangService.class);

    private final GioHangRepository repository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final TrangPhucRepository trangPhucRepository;

    public GioHangService(GioHangRepository repository, ChiTietGioHangRepository chiTietGioHangRepository,
            TrangPhucRepository trangPhucRepository) {
        this.repository = repository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.trangPhucRepository = trangPhucRepository;
    }

    public GioHang getOrCreate(Long khachHangId){
        GioHang gioHang = repository.findByKhachHangId(khachHangId).orElse(null);

        if(gioHang == null){
            gioHang = new GioHang();
            gioHang.setKhachHangId(khachHangId);
            gioHang.setNgayTao(LocalDate.now());
            gioHang.setNgayCapNhat(LocalDate.now());

            repository.save(gioHang);
        }

        return gioHang;
    }

    public LinkedHashMap<TrangPhuc, Integer> getCartItems(Long khachHangId){
        GioHang gioHang = getOrCreate(khachHangId);

        List<ChiTietGioHang> chiTiets = chiTietGioHangRepository.findByGioHangId(gioHang.getId());
        LinkedHashMap<TrangPhuc, Integer> items = new LinkedHashMap<>();

        for(ChiTietGioHang chiTiet : chiTiets){
            TrangPhuc tp = trangPhucRepository.findById(chiTiet.getTrangPhucId()).orElse(null);
            if(tp != null) items.put(tp, chiTiet.getSoLuong());
        }

        return items;
    }

    @Transactional
    public void addToCart(Long khachHangId, Long trangPhucId){
        GioHang cart = getOrCreate(khachHangId);
        ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findByGioHangIdAndTrangPhucId(cart.getId(), trangPhucId)
                                                                .orElse(null);
        if(chiTietGioHang == null){
            chiTietGioHang = new ChiTietGioHang();
            chiTietGioHang.setGioHangId(cart.getId());
            chiTietGioHang.setTrangPhucId(trangPhucId);
        }
        else{
            chiTietGioHang.setSoLuong(chiTietGioHang.getSoLuong() + 1);
        }
        chiTietGioHangRepository.save(chiTietGioHang);

        cart.setNgayCapNhat(LocalDate.now());

        repository.save(cart);
        log.info("Da them trang phuc {} vao gio hang khach {}", trangPhucId, khachHangId);
    }

    @Transactional
    public void removeFromCart(Long khachHangId, Long trangPhucId){
        GioHang cart = getOrCreate(khachHangId);

        chiTietGioHangRepository.deleteByGioHangIdAndTrangPhucId(cart.getId(), trangPhucId);
        log.info("Da xoa trang phuc {} khoi gio hang khach {}", trangPhucId, khachHangId);
    }
}
