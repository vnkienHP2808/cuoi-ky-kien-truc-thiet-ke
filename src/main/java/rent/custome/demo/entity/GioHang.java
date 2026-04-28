package rent.custome.demo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "gio_hang")
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "khach_hang_id", unique = true, nullable = false)
    private Long khachHangId;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDate ngayCapNhat;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "gio_hang_id")
    private List<ChiTietGioHang> chiTiet = new ArrayList<>();

    public GioHang() {
    }

    public GioHang(Long id, Long khachHangId, LocalDate ngayTao, LocalDate ngayCapNhat, List<ChiTietGioHang> chiTiet) {
        this.id = id;
        this.khachHangId = khachHangId;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.chiTiet = chiTiet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKhachHangId() {
        return khachHangId;
    }

    public void setKhachHangId(Long khachHangId) {
        this.khachHangId = khachHangId;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDate getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDate ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public List<ChiTietGioHang> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietGioHang> chiTiet) {
        this.chiTiet = chiTiet;
    }

    
}
