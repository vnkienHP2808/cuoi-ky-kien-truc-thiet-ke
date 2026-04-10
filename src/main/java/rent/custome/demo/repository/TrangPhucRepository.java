package rent.custome.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rent.custome.demo.entity.TrangPhuc;

@Repository
public interface TrangPhucRepository extends JpaRepository<TrangPhuc, Long>{

}
