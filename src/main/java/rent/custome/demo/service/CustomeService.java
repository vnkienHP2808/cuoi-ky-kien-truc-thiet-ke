package rent.custome.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import rent.custome.demo.entity.Custome;
import rent.custome.demo.repository.CustomeRepository;

@Service
public class CustomeService {

    private final CustomeRepository repository;

    public CustomeService(CustomeRepository repository) {
        this.repository = repository;
    }

    public List<Custome> findAll(){
        return repository.findAll();
    }

    public Optional<Custome> findById(Long id){
        return repository.findById(id);
    }
}
