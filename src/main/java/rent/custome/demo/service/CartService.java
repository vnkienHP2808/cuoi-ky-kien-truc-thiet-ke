package rent.custome.demo.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rent.custome.demo.entity.CartItem;
import rent.custome.demo.entity.Cart;
import rent.custome.demo.entity.Custome;
import rent.custome.demo.repository.CartItemRepository;
import rent.custome.demo.repository.CartRepository;
import rent.custome.demo.repository.CustomeRepository;

@Service
public class CartService {
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository repository;
    private final CartItemRepository chiTietGioHangRepository;
    private final CustomeRepository trangPhucRepository;

    public CartService(CartRepository repository, CartItemRepository chiTietGioHangRepository,
            CustomeRepository trangPhucRepository) {
        this.repository = repository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.trangPhucRepository = trangPhucRepository;
    }

    public Cart getOrCreate(Long khachHangId){
        Cart gioHang = repository.findByKhachHangId(khachHangId).orElse(null);

        if(gioHang == null){
            gioHang = new Cart();
            gioHang.setKhachHangId(khachHangId);
            gioHang.setNgayTao(LocalDate.now());
            gioHang.setNgayCapNhat(LocalDate.now());

            repository.save(gioHang);
        }

        log.info("Lay gio hang cho khachHangId={}, gioHangId={}", khachHangId, gioHang.getId());

        return gioHang;
    }

    public LinkedHashMap<Custome, Integer> getCartItems(Long khachHangId){
        Cart gioHang = getOrCreate(khachHangId);

        List<CartItem> chiTiets = chiTietGioHangRepository.findByGioHangId(gioHang.getId());
        LinkedHashMap<Custome, Integer> items = new LinkedHashMap<>();

        for(CartItem chiTiet : chiTiets){
            Custome tp = trangPhucRepository.findById(chiTiet.getTrangPhucId()).orElse(null);
            if(tp != null) items.put(tp, chiTiet.getSoLuong());
        }

        log.info("Lay {} trang phuc trong gio hang khachHangId={}", items.size(), khachHangId);

        return items;
    }

    @Transactional
    public void addToCart(Long khachHangId, Long trangPhucId){
        Custome tp = trangPhucRepository.findById(trangPhucId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trang phục"));

        Cart cart = getOrCreate(khachHangId);
        CartItem chiTietGioHang = chiTietGioHangRepository.findByGioHangIdAndTrangPhucId(cart.getId(), trangPhucId)
                                                                .orElse(null);
        
        if(chiTietGioHang == null){
            chiTietGioHang = new CartItem();
            chiTietGioHang.setGioHangId(cart.getId());
            chiTietGioHang.setTrangPhucId(trangPhucId);
            chiTietGioHang.setSoLuong(1);
        }
        else{
            if (chiTietGioHang.getSoLuong() >= tp.getSoLuong()) {
                 throw new RuntimeException("So luong da dat muc toi da trong gio hang");
            }
            chiTietGioHang.setSoLuong(chiTietGioHang.getSoLuong() + 1);
        }
        chiTietGioHangRepository.save(chiTietGioHang);

        cart.setNgayCapNhat(LocalDate.now());
        repository.save(cart);
        
        log.info("Da them trang phuc {} vao gio hang khach {}", trangPhucId, khachHangId);
    }

    @Transactional
    public void removeFromCart(Long khachHangId, Long trangPhucId){
        Cart cart = getOrCreate(khachHangId);

        chiTietGioHangRepository.deleteByGioHangIdAndTrangPhucId(cart.getId(), trangPhucId);
        log.info("Da xoa trang phuc {} khoi gio hang khach {}", trangPhucId, khachHangId);
    }
}
