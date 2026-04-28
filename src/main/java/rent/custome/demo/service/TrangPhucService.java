package rent.custome.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.repository.TrangPhucRepository;

@Service
public class TrangPhucService {
    private static final Logger log = LoggerFactory.getLogger(TrangPhucService.class);

    private final TrangPhucRepository repository;

    public TrangPhucService(TrangPhucRepository repository) {
        this.repository = repository;
    }

    public List<TrangPhuc> findAll(){
        List<TrangPhuc> all = repository.findAll();

        log.info("Lay tat ca trang phuc, so luong={}", all.size());
        return all;
    }

    public Optional<TrangPhuc> findById(Long id){
        Optional<TrangPhuc> result = repository.findById(id);

        log.info("Lay trang phuc id={}", id);
        return result;
    }
}
