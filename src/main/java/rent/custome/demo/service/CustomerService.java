package rent.custome.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import rent.custome.demo.entity.Customer;
import rent.custome.demo.repository.CustomerRepository;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<Customer> findAll(){
        return repo.findAll();
    }

    public Optional<Customer> findById(Long id){
        return repo.findById(id);
    }

    public void create(Customer khachHang){
        if(repo.findByUsername(khachHang.getUsername()).isPresent()){
            throw new RuntimeException("Username '" + khachHang.getUsername() + "' da ton tai");
        }

        Customer kh = new Customer();
        kh.setHoTen(khachHang.getHoTen());
        kh.setUsername(khachHang.getUsername());
        kh.setPassword(khachHang.getPassword());
        kh.setEmail(khachHang.getEmail());
        kh.setSoDienThoai(khachHang.getSoDienThoai());
        kh.setDiaChi(khachHang.getDiaChi());
        kh.setDob(khachHang.getDob());
        kh.setRole(khachHang.getRole() != null && !khachHang.getRole().isBlank() ? khachHang.getRole() : "customer");
        kh.setIsActive(khachHang.getIsActive() != null ? khachHang.getIsActive() : true);

        repo.save(kh);

        log.info("Tao khach hang moi hoTen={}", khachHang.getHoTen());
    }

    public void update(Long id, Customer khachHang){
        Customer kh = repo.findById(id).orElseThrow(
            () -> new RuntimeException("Khong tim thay khach hang co id=" + id));

        kh.setHoTen(khachHang.getHoTen());
        kh.setPassword(khachHang.getPassword());
        kh.setEmail(khachHang.getEmail());
        kh.setSoDienThoai(khachHang.getSoDienThoai());
        kh.setDiaChi(khachHang.getDiaChi());
        kh.setDob(khachHang.getDob());
        if (khachHang.getRole() != null && !khachHang.getRole().isBlank()) {
            kh.setRole(khachHang.getRole());
        }
        if (khachHang.getIsActive() != null) {
            kh.setIsActive(khachHang.getIsActive());
        }

        repo.save(kh);

        log.info("Cap nhat khach hang hoTen={}", khachHang.getHoTen());
    }

    public void doiTrangThai(Long khachHangId){

        Customer kh = repo.findById(khachHangId).orElseThrow(
            () -> new RuntimeException("Khong tim thay khach hang co id=" + khachHangId));

            kh.setIsActive(!Boolean.TRUE.equals(kh.getIsActive()));
            repo.save(kh);

            log.info("Cap nhat trang thai tai khoan khach hang id = " + khachHangId);
    }
}
