package rent.custome.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import rent.custome.demo.entity.Custome;
import rent.custome.demo.repository.CustomeRepository;

@Service
public class CustomeService {
    private static final Logger log = LoggerFactory.getLogger(CustomeService.class);

    private final CustomeRepository repository;

    public CustomeService(CustomeRepository repository) {
        this.repository = repository;
    }

    public List<Custome> findAll(){
        List<Custome> all = repository.findAll();

        log.info("Lay tat ca trang phuc, so luong={}", all.size());
        return all;
    }

    public Optional<Custome> findById(Long id){
        Optional<Custome> result = repository.findById(id);

        log.info("Lay trang phuc id={}", id);
        return result;
    }
}
