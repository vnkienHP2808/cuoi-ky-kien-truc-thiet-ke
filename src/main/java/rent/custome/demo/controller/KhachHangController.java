package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.dto.KhachHangDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class KhachHangController {

    private final KhachHangService service;

    public KhachHangController(KhachHangService service) {
        this.service = service;
    }

    @ModelAttribute("allKhachHangs")
    public List<KhachHang> allKhachHangs() {
        return service.findAll();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("khachHangs", service.findAll());
        return "admin/list";
    }

    @GetMapping("/them")
    public String showCreateForm(Model model) {
        model.addAttribute("dto", new KhachHangDto());
        model.addAttribute("editing", false);
        return "admin/form";
    }

    @PostMapping("/them")
    public String create(@ModelAttribute("dto") KhachHangDto dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("editing", false);
            return "admin/form";
        }
        try {
            service.create(dto);
            ra.addFlashAttribute("success", "Đã thêm khách hàng thành công");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("editing", false);
            return "admin/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        KhachHang kh = service.findById(id).orElse(null);
        if (kh == null) {
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng id=" + id);
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
        KhachHangDto dto = new KhachHangDto();
        dto.setHoTen(kh.getHoTen());
        dto.setUsername(kh.getUsername());
        dto.setEmail(kh.getEmail());
        dto.setSoDienThoai(kh.getSoDienThoai());
        dto.setDiaChi(kh.getDiaChi());
        dto.setDob(kh.getDob());
        dto.setRole(kh.getRole());
        dto.setIsActive(kh.getIsActive());
        model.addAttribute("dto", dto);
        model.addAttribute("editing", true);
        model.addAttribute("khId", id);
        return "admin/form";
    }

    @PostMapping("/{id}/sua")
    public String update(@PathVariable Long id,
                         @ModelAttribute("dto") KhachHangDto dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("editing", true);
            model.addAttribute("khId", id);
            return "admin/form";
        }
        try {
            service.update(id, dto);
            ra.addFlashAttribute("success", "Đã cập nhật thành công");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("editing", true);
            model.addAttribute("khId", id);
            return "admin/form";
        }
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.toggleTrangThai(id);
            ra.addFlashAttribute("success", "Đã thay đổi trạng thái");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
