package rent.custome.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.annotation.RequireRole;
import rent.custome.demo.dto.KhachHangDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

@Controller
@RequestMapping("/admin")
@RequireRole("admin")  // Toàn bộ controller chỉ dành cho admin
public class KhachHangController {

    private final KhachHangService service;

    public KhachHangController(KhachHangService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("khachHangs", service.findAll());
        return "admin/list";
    }

    @GetMapping("/them")
    public String showAddForm(Model model) {
        model.addAttribute("form", new KhachHangDto());
        model.addAttribute("isEdit", false);
        return "admin/form";
    }

    @PostMapping("/them")
    public String create(@Valid @ModelAttribute("form") KhachHangDto form,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/form";
        }
        try {
            service.create(form);
            ra.addFlashAttribute("success", "Tạo mới khách hàng thành công");
        } catch (Exception e) {
            model.addAttribute("isEdit", false);
            model.addAttribute("error", e.getMessage());
            return "admin/form";
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        KhachHang kh = service.findById(id).orElse(null);
        if (kh == null) {
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin";
        }
        model.addAttribute("kh", kh);
        return "admin/detail";
    }

    @GetMapping("/{id}/sua")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        KhachHang kh = service.findById(id).orElse(null);
        if (kh == null) {
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin";
        }
        KhachHangDto form = new KhachHangDto();
        form.setHoTen(kh.getHoTen()); form.setUsername(kh.getUsername());
        form.setPassword(kh.getPassword()); form.setEmail(kh.getEmail());
        form.setSoDienThoai(kh.getSoDienThoai()); form.setDiaChi(kh.getDiaChi());
        form.setDob(kh.getDob()); form.setRole(kh.getRole());
        form.setIsActive(kh.getIsActive());
        model.addAttribute("form", form);
        model.addAttribute("khachHangId", id);
        model.addAttribute("isEdit", true);
        return "admin/form";
    }

    @PostMapping("/{id}/sua")
    public String update(@Valid @ModelAttribute("form") KhachHangDto form,
                         BindingResult br, @PathVariable Long id,
                         Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("khachHangId", id);
            model.addAttribute("isEdit", true);
            return "admin/form";
        }
        try {
            service.update(id, form);
            ra.addFlashAttribute("success", "Cập nhật khách hàng " + id + " thành công");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("khachHangId", id);
            model.addAttribute("isEdit", true);
            return "admin/form";
        }
        return "redirect:/admin";
    }

    @PostMapping("/{id}/doi-trang-thai")
    public String toggleTrangThai(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.toggleTrangThai(id);
            ra.addFlashAttribute("success", "Đổi trạng thái tài khoản thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
