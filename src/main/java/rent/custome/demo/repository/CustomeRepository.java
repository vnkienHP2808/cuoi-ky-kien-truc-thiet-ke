package rent.custome.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.Custome;

@Repository
public interface CustomeRepository extends JpaRepository<Custome, Long>{

}
