package rent.custome.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.repository.KhachHangRepository;

@Service
public class KhachHangService {

    private static final Logger log = LoggerFactory.getLogger(KhachHangService.class);

    private final KhachHangRepository repo;

    public KhachHangService(KhachHangRepository repo) {
        this.repo = repo;
    }

    public List<KhachHang> findAll(){
        return repo.findAll();
    }

    public Optional<KhachHang> findById(Long id){
        return repo.findById(id);
    }

    public KhachHang create(KhachHang dto){
        log.info("Tao khach hang username={}", dto.getUsername());
        if(repo.findByUsername(dto.getUsername()).isPresent()){
            throw new RuntimeException("Username '" + dto.getUsername() + "' da ton tai");
        }

        KhachHang kh = new KhachHang();
        kh.setHoTen(dto.getHoTen());
        kh.setUsername(dto.getUsername());
        kh.setPassword(dto.getPassword());
        kh.setEmail(dto.getEmail());
        kh.setSoDienThoai(dto.getSoDienThoai());
        kh.setDiaChi(dto.getDiaChi());
        kh.setDob(dto.getDob());
        kh.setRole(dto.getRole() != null && !dto.getRole().isBlank() ? dto.getRole() : "customer");
        kh.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return repo.save(kh);
    }

    public KhachHang update(Long id, KhachHang dto){
        log.info("Cap nhat khach hang hoTen={}", dto.getHoTen());

        KhachHang kh = repo.findById(id).orElseThrow(
            () -> new RuntimeException("Khong tim thay khach hang co id=" + id));

        kh.setHoTen(dto.getHoTen());
        kh.setPassword(dto.getPassword());
        kh.setEmail(dto.getEmail());
        kh.setSoDienThoai(dto.getSoDienThoai());
        kh.setDiaChi(dto.getDiaChi());
        kh.setDob(dto.getDob());
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            kh.setRole(dto.getRole());
        }
        if (dto.getIsActive() != null) {
            kh.setIsActive(dto.getIsActive());
        }

        return repo.save(kh);
    }
}
