package rent.custome.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.GioHang;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Long>{

}
