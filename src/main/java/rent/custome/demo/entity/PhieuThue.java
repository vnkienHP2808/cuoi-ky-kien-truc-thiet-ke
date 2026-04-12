package rent.custome.demo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import rent.custome.demo.enums.HinhThucThue;
import rent.custome.demo.enums.PhieuThueStatus;
import rent.custome.demo.enums.TrangThaiDatCoc;
import rent.custome.demo.state.PhieuThueState;
import rent.custome.demo.state.PhieuThueStateFactory;

@Entity
@Table(name = "phieu_thue")
public class PhieuThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "khach_hang_id")
    private Long khachHangId;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_hen_lay")
    private LocalDate ngayHenLay;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "phieu_thue_id")
    private List<ChiTietPhieuThue> chiTiet = new ArrayList<>();

    /**
     * STATE PATTERN - không persist, khôi phục từ enum trangThai khi cần.
     */
    @Transient
    private PhieuThueState currentState;

    // --- STATE API ---

    public PhieuThueState getState() {
        if (currentState == null) {
            currentState = PhieuThueStateFactory.from(this.trangThai);
        }
        return currentState;
    }

    public void applyState(PhieuThueState newState) {
        this.currentState = newState;
    }

    public void datCoc() {
        getState().datCoc(this);
    }

    public void huy() {
        getState().huy(this);
    }

    public String getTenTrangThai() {
        return getState().getTenTrangThai();
    }

    // --- Constructors ---

    public PhieuThue() {}

    public PhieuThue(Long id, Long khachHangId, LocalDate ngayTao,
                     LocalDate ngayHenLay, LocalDate ngayHenTra,
                     HinhThucThue hinhThuc, PhieuThueStatus trangThai,
                     Double tienDatCoc, TrangThaiDatCoc trangThaiDatCoc,
                     List<ChiTietPhieuThue> chiTiet) {
        this.id = id; this.khachHangId = khachHangId; this.ngayTao = ngayTao;
        this.ngayHenLay = ngayHenLay; this.ngayHenTra = ngayHenTra;
        this.hinhThuc = hinhThuc; this.trangThai = trangThai;
        this.tienDatCoc = tienDatCoc; this.trangThaiDatCoc = trangThaiDatCoc;
        this.chiTiet = chiTiet;
    }

    // --- Getters / Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getKhachHangId() { return khachHangId; }
    public void setKhachHangId(Long khachHangId) { this.khachHangId = khachHangId; }

    public LocalDate getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDate ngayTao) { this.ngayTao = ngayTao; }

    public LocalDate getNgayHenLay() { return ngayHenLay; }
    public void setNgayHenLay(LocalDate ngayHenLay) { this.ngayHenLay = ngayHenLay; }

    public LocalDate getNgayHenTra() { return ngayHenTra; }
    public void setNgayHenTra(LocalDate ngayHenTra) { this.ngayHenTra = ngayHenTra; }

    public HinhThucThue getHinhThuc() { return hinhThuc; }
    public void setHinhThuc(HinhThucThue hinhThuc) { this.hinhThuc = hinhThuc; }

    public PhieuThueStatus getTrangThai() { return trangThai; }
    public void setTrangThai(PhieuThueStatus trangThai) {
        this.trangThai = trangThai;
        this.currentState = null; // reset để lazy-init lại
    }

    public Double getTienDatCoc() { return tienDatCoc; }
    public void setTienDatCoc(Double tienDatCoc) { this.tienDatCoc = tienDatCoc; }

    public TrangThaiDatCoc getTrangThaiDatCoc() { return trangThaiDatCoc; }
    public void setTrangThaiDatCoc(TrangThaiDatCoc tdc) { this.trangThaiDatCoc = tdc; }

    public List<ChiTietPhieuThue> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietPhieuThue> chiTiet) { this.chiTiet = chiTiet; }
}
