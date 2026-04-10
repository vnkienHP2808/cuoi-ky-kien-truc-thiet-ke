package rent.custome.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.KhachHang;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long>{

    Optional<KhachHang> findByUsername(String username);

}
