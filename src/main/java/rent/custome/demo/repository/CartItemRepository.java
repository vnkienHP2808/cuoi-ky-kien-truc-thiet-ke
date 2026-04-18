package rent.custome.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    List<CartItem> findByGioHangId(Long gioHangId);
    Optional<CartItem> findByGioHangIdAndTrangPhucId(Long gioHangId, Long trangPhucId);
    void deleteByGioHangIdAndTrangPhucId(Long gioHangId, Long trangPhucId);
}
