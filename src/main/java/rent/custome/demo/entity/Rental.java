package rent.custome.demo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import rent.custome.demo.enums.HinhThucThue;
import rent.custome.demo.enums.PhieuThueStatus;
import rent.custome.demo.enums.TrangThaiDatCoc;

@Entity
@Table(name = "phieu_thue")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "khach_hang_id")
    private Long khachHangId;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @NotNull(message = "Ngày hẹn lấy không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "ngay_hen_lay")
    private LocalDate ngayHenLay;

    @NotNull(message = "Ngày hẹn trả không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "ngay_hen_tra")
    private LocalDate ngayHenTra;

    @Enumerated(EnumType.STRING)
    @Column(name = "hinh_thuc")
    private HinhThucThue hinhThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private PhieuThueStatus trangThai;

    @Column(name = "tien_dat_coc")
    private Double tienDatCoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_dat_coc")
    private TrangThaiDatCoc trangThaiDatCoc;

    @OneToMany
    @JoinColumn(name = "phieu_thue_id")
    private List<RentalItem> chiTiet = new ArrayList<>();

    public Rental() {}

    public Rental(Long id, Long khachHangId, LocalDate ngayTao,
                     LocalDate ngayHenLay, LocalDate ngayHenTra,
                     HinhThucThue hinhThuc, PhieuThueStatus trangThai,
                     Double tienDatCoc, TrangThaiDatCoc trangThaiDatCoc,
                     List<RentalItem> chiTiet) {
        this.id = id; 
        this.khachHangId = khachHangId; 
        this.ngayTao = ngayTao;
        this.ngayHenLay = ngayHenLay;
        this.ngayHenTra = ngayHenTra;
        this.hinhThuc = hinhThuc; 
        this.trangThai = trangThai;
        this.tienDatCoc = tienDatCoc; 
        this.trangThaiDatCoc = trangThaiDatCoc;
        this.chiTiet = chiTiet;
    }

    // --- Getters / Setters ---

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

    public LocalDate getNgayHenLay() {
        return ngayHenLay;
    }

    public void setNgayHenLay(LocalDate ngayHenLay) {
        this.ngayHenLay = ngayHenLay;
    }

    public LocalDate getNgayHenTra() {
        return ngayHenTra;
    }

    public void setNgayHenTra(LocalDate ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }

    public HinhThucThue getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(HinhThucThue hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public Double getTienDatCoc() {
        return tienDatCoc;
    }

    public void setTienDatCoc(Double tienDatCoc) {
        this.tienDatCoc = tienDatCoc;
    }

    public TrangThaiDatCoc getTrangThaiDatCoc() {
        return trangThaiDatCoc;
    }

    public void setTrangThaiDatCoc(TrangThaiDatCoc trangThaiDatCoc) {
        this.trangThaiDatCoc = trangThaiDatCoc;
    }

    public List<RentalItem> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<RentalItem> chiTiet) {
        this.chiTiet = chiTiet;
    }

    public PhieuThueStatus getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(PhieuThueStatus trangThai) {
        this.trangThai = trangThai;
    }

}
