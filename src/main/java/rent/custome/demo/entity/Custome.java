package rent.custome.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import rent.custome.demo.enums.TrangPhucStatus;

@Entity
@Table(name = "trang_phuc")
public class Custome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ten_trang_phuc")
    private String tenTrangPhuc;
    
    @ManyToOne
    @JoinColumn(name = "loai_trang_phuc_id")
    private CustomeType loaiTrangPhuc;


    @Column(name = "kich_thuoc")
    private String kichThuoc;

    @Column(name = "mau_sac")
    private String mauSac;

    @Column(name = "gia_thue")
    private Double giaThue;

    @Column(name = "gia_goc")
    private Double giaGoc;

    @Column(name = "so_luong")
    private Integer soLuong = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangPhucStatus trangThai;

    public Custome() {
    }
    
    public Custome(Long id, String tenTrangPhuc, CustomeType loaiTrangPhuc, String kichThuoc, String mauSac,
            Double giaThue, Double giaGoc, Integer soLuong, TrangPhucStatus trangThai) {
        this.id = id;
        this.tenTrangPhuc = tenTrangPhuc;
        this.loaiTrangPhuc = loaiTrangPhuc;
        this.kichThuoc = kichThuoc;
        this.mauSac = mauSac;
        this.giaThue = giaThue;
        this.giaGoc = giaGoc;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenTrangPhuc() {
        return tenTrangPhuc;
    }

    public void setTenTrangPhuc(String tenTrangPhuc) {
        this.tenTrangPhuc = tenTrangPhuc;
    }

    public CustomeType getLoaiTrangPhuc() {
        return loaiTrangPhuc;
    }

    public void setLoaiTrangPhuc(CustomeType loaiTrangPhuc) {
        this.loaiTrangPhuc = loaiTrangPhuc;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public Double getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(Double giaThue) {
        this.giaThue = giaThue;
    }

    public Double getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(Double giaGoc) {
        this.giaGoc = giaGoc;
    }

    public TrangPhucStatus getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangPhucStatus trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    
}
