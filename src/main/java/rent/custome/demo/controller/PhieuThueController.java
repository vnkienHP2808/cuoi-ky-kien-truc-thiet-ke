package rent.custome.demo.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.config.AppConfig;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.repository.KhachHangRepository;
import rent.custome.demo.repository.TrangPhucRepository;
import rent.custome.demo.service.PhieuThueService;

@Controller
@RequestMapping("/phieu-thue")
public class PhieuThueController {

    private final PhieuThueService service;
    private final TrangPhucRepository trangPhucRepository;
    private final KhachHangRepository khachHangRepository;
    private final AppConfig appConfig;

    public PhieuThueController(PhieuThueService service,
                                TrangPhucRepository trangPhucRepository,
                                KhachHangRepository khachHangRepository,
                                AppConfig appConfig) {
        this.service = service;
        this.trangPhucRepository = trangPhucRepository;
        this.khachHangRepository = khachHangRepository;
        this.appConfig = appConfig;
    }

    // ── Tạo phiếu thuê ──────────────────────────────────────────────

    @GetMapping("/tao")
    public String showCreateForm(Model model) {
        Long khachHangId = appConfig.getKhachHangId();
        KhachHang kh = findKhOrThrow(khachHangId);
        model.addAttribute("kh", kh);
        model.addAttribute("pt", new PhieuThue());
        model.addAttribute("selectedKhachHangId", khachHangId);
        return "phieu-thue/tao";
    }

    @PostMapping("/tao")
    public String create(@Valid @ModelAttribute("pt") PhieuThue pt,
                         BindingResult br, Model model, RedirectAttributes ra) {
        Long khachHangId = appConfig.getKhachHangId();
        KhachHang kh = findKhOrThrow(khachHangId);
        if (br.hasErrors()) {
            model.addAttribute("kh", kh);
            model.addAttribute("selectedKhachHangId", khachHangId);
            return "phieu-thue/tao";
        }
        try {
            PhieuThue saved = service.createFromCart(khachHangId, pt);
            ra.addFlashAttribute("success", "Đã tạo phiếu thuê #" + saved.getId());
            return "redirect:/phieu-thue/" + saved.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("kh", kh);
            model.addAttribute("selectedKhachHangId", khachHangId);
            return "phieu-thue/tao";
        }
    }

    // ── Danh sách phiếu của khách ────────────────────────────────────

    @GetMapping("/cua-toi")
    public String showListPhieuThue(Model model) {
        Long khachHangId = appConfig.getKhachHangId();
        KhachHang kh = findKhOrThrow(khachHangId);
        model.addAttribute("kh", kh);
        model.addAttribute("phieus", service.findByKhachHangId(khachHangId));
        model.addAttribute("selectedKhachHangId", khachHangId);
        return "phieu-thue/list-by-kh";
    }

    // ── Đặt cọc ──────────────────────────────────────────────────────

    @PostMapping("/{id}/dat-coc")
    public String datCoc(@PathVariable Long id, RedirectAttributes ra) {
        Long khachHangId = appConfig.getKhachHangId();
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu");
            return "redirect:/phieu-thue/cua-toi";
        }
        if (!pt.getKhachHangId().equals(khachHangId)) {
            ra.addFlashAttribute("error", "Phiếu này không thuộc khách hàng đang chọn");
            return "redirect:/phieu-thue/cua-toi";
        }
        try {
            service.datCoc(id);
            ra.addFlashAttribute("success", "Đặt cọc thành công. Vui lòng đến lấy đúng hẹn");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/phieu-thue/" + id;
    }

    // ── Hủy phiếu ────────────────────────────────────────────────────

    @PostMapping("/{id}/huy")
    public String huyPhieu(@PathVariable Long id, RedirectAttributes ra) {
        Long khachHangId = appConfig.getKhachHangId();
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu");
            return "redirect:/phieu-thue/cua-toi";
        }
        if (!pt.getKhachHangId().equals(khachHangId)) {
            ra.addFlashAttribute("error", "Phiếu này không thuộc khách hàng đang chọn");
            return "redirect:/phieu-thue/cua-toi";
        }
        try {
            service.huyPhieu(id);
            ra.addFlashAttribute("success", "Đã hủy phiếu thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/phieu-thue/" + id;
    }

    // ── Chi tiết phiếu ───────────────────────────────────────────────

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Long khachHangId = appConfig.getKhachHangId();
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu thuê");
            return "redirect:/phieu-thue/cua-toi";
        }
        if (!pt.getKhachHangId().equals(khachHangId)) {
            ra.addFlashAttribute("error", "Phiếu này không thuộc khách hàng đang chọn");
            return "redirect:/phieu-thue/cua-toi";
        }
        KhachHang kh = findKhOrThrow(khachHangId);
        model.addAttribute("kh", kh);
        model.addAttribute("pt", pt);
        model.addAttribute("trangPhucs", pt.getChiTiet().stream()
                .map(ct -> trangPhucRepository.findById(ct.getTrangPhucId()).orElse(null))
                .toList());
        model.addAttribute("selectedKhachHangId", khachHangId);
        return "phieu-thue/detail";
    }

    // ── Helper ───────────────────────────────────────────────────────

    private KhachHang findKhOrThrow(Long id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id=" + id));
    }
}
