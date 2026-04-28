package rent.custome.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "loai_trang_phuc")
public class LoaiTrangPhuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_loai_trang_phuc")
    private String tenLoaiTrangPhuc;

    @Column(name = "mo_ta")
    private String moTa;

    public LoaiTrangPhuc() {
    }

    public LoaiTrangPhuc(Long id, String tenLoaiTrangPhuc, String moTa) {
        this.id = id;
        this.tenLoaiTrangPhuc = tenLoaiTrangPhuc;
        this.moTa = moTa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenLoaiTrangPhuc() {
        return tenLoaiTrangPhuc;
    }

    public void setTenLoaiTrangPhuc(String tenLoaiTrangPhuc) {
        this.tenLoaiTrangPhuc = tenLoaiTrangPhuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    
}
