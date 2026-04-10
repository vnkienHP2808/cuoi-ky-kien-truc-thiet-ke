package rent.custome.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.repository.TrangPhucRepository;

@Service
public class TrangPhucService {

    private final TrangPhucRepository repository;

    public TrangPhucService(TrangPhucRepository repository) {
        this.repository = repository;
    }

    public List<TrangPhuc> findAll(){
        return repository.findAll();
    }

    public List<TrangPhuc> findAllLoai(){
        return repository.findAll();
    }

    public Optional<TrangPhuc> findById(Long id){
        return repository.findById(id);
    }
}
