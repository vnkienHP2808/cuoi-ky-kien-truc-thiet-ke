package rent.custome.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>{
    List<Rental> findByKhachHangId(Long khachHangId);
}
