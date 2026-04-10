package rent.custome.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.ChiTietGioHang;

@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Long>{
    List<ChiTietGioHang> findByGioHangId(Long gioHangId);
    Optional<ChiTietGioHang> findByGioHangIdAndTrangPhucId(Long gioHangId, Long trangPhucId);
    void deleteByGioHangIdAndTrangPhucId(Long gioHangId, Long trangPhucId);
}
