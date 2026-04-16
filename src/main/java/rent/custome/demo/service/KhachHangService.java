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

    public void create(KhachHang khachHang){
        log.info("Tao khach hang username={}", khachHang.getUsername());
        if(repo.findByUsername(khachHang.getUsername()).isPresent()){
            throw new RuntimeException("Username '" + khachHang.getUsername() + "' da ton tai");
        }

        KhachHang kh = new KhachHang();
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
    }

    public void update(Long id, KhachHang khachHang){
        log.info("Cap nhat khach hang hoTen={}", khachHang.getHoTen());

        KhachHang kh = repo.findById(id).orElseThrow(
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
    }

    public void doiTrangThai(Long khachHangId){
        log.info("Cap nhat trang thai tai khoan khach hang id = " + khachHangId);

        KhachHang kh = repo.findById(khachHangId).orElseThrow(
            () -> new RuntimeException("Khong tim thay khach hang co id=" + khachHangId));

            kh.setIsActive(!Boolean.TRUE.equals(kh.getIsActive()));
            repo.save(kh);
    }
}
