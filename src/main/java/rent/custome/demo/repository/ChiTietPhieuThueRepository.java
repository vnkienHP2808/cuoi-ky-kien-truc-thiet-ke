package rent.custome.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.ChiTietPhieuThue;

@Repository
public interface ChiTietPhieuThueRepository extends JpaRepository<ChiTietPhieuThue, Long> {
    List<ChiTietPhieuThue> findByPhieuThueId(Long phieuThueId);
}
