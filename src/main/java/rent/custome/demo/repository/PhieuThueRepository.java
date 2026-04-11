package rent.custome.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.PhieuThue;

@Repository
public interface PhieuThueRepository extends JpaRepository<PhieuThue, Long>{
    List<PhieuThue> findByKhachHangId(Long khachHangId);
}
