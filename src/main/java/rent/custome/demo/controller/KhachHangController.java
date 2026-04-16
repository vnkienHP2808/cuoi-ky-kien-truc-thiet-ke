package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;


@Controller
@RequestMapping("/admin")
public class KhachHangController {

    private final KhachHangService service;

    public KhachHangController(KhachHangService service) {
        this.service = service;
    }

    @GetMapping
    public String showListKhachHang(Model model) {
        model.addAttribute("khachHangs", service.findAll());
        return "admin/list";
    }

    @GetMapping("/them")
    public String showCreateForm(Model model) {
        model.addAttribute("form", new KhachHang());
        model.addAttribute("isEdit", false);
        return "admin/form";
    }

    @PostMapping("/them")
    public String create(@ModelAttribute("form") KhachHang khachHang,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/form";
        }
        try {
            service.create(khachHang);
            ra.addFlashAttribute("success", "Đã thêm khách hàng thành công");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isEdit", false);
            return "admin/form";
        }
    }

    @GetMapping("/{id}/sua")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        KhachHang kh = service.findById(id).orElse(null);
        if (kh == null) {
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin";
        }
        model.addAttribute("form", kh);
        model.addAttribute("isEdit", true);
        model.addAttribute("khachHangId", id);
        return "admin/form";
    }

    @PostMapping("/{id}/sua")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") KhachHang khachHang,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("khachHangId", id);
            return "admin/form";
        }
        try {
            service.update(id, khachHang);
            ra.addFlashAttribute("success", "Đã cập nhật thành công");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("khachHangId", id);
            return "admin/form";
        }
    }

    @PostMapping("/{id}/doi-trang-thai")
    public String doiTrangThai(@PathVariable Long id, RedirectAttributes ra){
        try {
            service.doiTrangThai(id);
            ra.addFlashAttribute("success", "Đã đổi trạng thái tài khoản thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
