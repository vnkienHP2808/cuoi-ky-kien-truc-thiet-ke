package rent.custome.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.annotation.RequireRole;
import rent.custome.demo.dto.CreateOrderRequest;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.repository.TrangPhucRepository;
import rent.custome.demo.service.PhieuThueService;

@Controller
@RequestMapping("/phieu-thue")
public class PhieuThueController {

    private final PhieuThueService service;
    private final TrangPhucRepository trangPhucRepository;

    public PhieuThueController(PhieuThueService service, TrangPhucRepository trangPhucRepository) {
        this.service = service;
        this.trangPhucRepository = trangPhucRepository;
    }

    @GetMapping("/tao")
    @RequireRole("customer")
    public String showCreateForm(HttpSession session, Model model) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        model.addAttribute("kh", kh);
        model.addAttribute("req", new CreateOrderRequest());
        return "phieu-thue/tao";
    }

    @PostMapping("/tao")
    @RequireRole("customer")
    public String create(HttpSession session,
                         @Valid @ModelAttribute("req") CreateOrderRequest req,
                         BindingResult br, Model model, RedirectAttributes ra) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (br.hasErrors()) { model.addAttribute("kh", kh); return "phieu-thue/tao"; }
        try {
            PhieuThue pt = service.createFromCart(kh.getId(), req);
            ra.addFlashAttribute("success", "Đã tạo phiếu thuê #" + pt.getId());
            return "redirect:/phieu-thue/" + pt.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("kh", kh);
            return "phieu-thue/tao";
        }
    }

    @GetMapping("/cua-toi")
    @RequireRole("customer")
    public String showListPhieuThue(HttpSession session, Model model) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        model.addAttribute("kh", kh);
        model.addAttribute("phieus", service.findByKhachHangId(kh.getId()));
        return "phieu-thue/list-by-kh";
    }

    @PostMapping("/{id}/dat-coc")
    @RequireRole("customer")
    public String datCoc(HttpSession session, @PathVariable Long id, RedirectAttributes ra) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) { ra.addFlashAttribute("error", "Không tìm thấy phiếu"); return "redirect:/phieu-thue/cua-toi"; }
        if (!pt.getKhachHangId().equals(kh.getId())) { ra.addFlashAttribute("error", "Bạn không có quyền thao tác phiếu này"); return "redirect:/phieu-thue/cua-toi"; }
        try {
            service.datCoc(id);
            ra.addFlashAttribute("success", "Đặt cọc thành công. Vui lòng đến lấy đúng hẹn");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/phieu-thue/" + id;
    }

    @PostMapping("/{id}/huy")
    @RequireRole("customer")
    public String huyPhieu(HttpSession session, @PathVariable Long id, RedirectAttributes ra) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) { ra.addFlashAttribute("error", "Không tìm thấy phiếu"); return "redirect:/phieu-thue/cua-toi"; }
        if (!pt.getKhachHangId().equals(kh.getId())) { ra.addFlashAttribute("error", "Bạn không có quyền hủy phiếu này"); return "redirect:/phieu-thue/cua-toi"; }
        try {
            service.huyPhieu(id);
            ra.addFlashAttribute("success", "Đã hủy phiếu thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/phieu-thue/" + id;
    }


    @GetMapping("/{id}")
    @RequireRole("customer")
    public String detail(HttpSession session, @PathVariable Long id,
                         Model model, RedirectAttributes ra) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu thuê");
            return "redirect:/phieu-thue/cua-toi";
        }

        if (!pt.getKhachHangId().equals(kh.getId())) {
            ra.addFlashAttribute("error", "Bạn không có quyền xem phiếu này");
            return "redirect:/phieu-thue/cua-toi";
        }
        model.addAttribute("kh", kh);
        model.addAttribute("pt", pt);
        model.addAttribute("trangPhucs", pt.getChiTiet().stream()
                .map(ct -> trangPhucRepository.findById(ct.getTrangPhucId()).orElse(null))
                .toList());
        return "phieu-thue/detail";
    }
}
