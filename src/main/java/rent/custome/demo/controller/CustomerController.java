package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import rent.custome.demo.entity.Customer;
import rent.custome.demo.service.CustomerService;


@Controller
@RequestMapping("/admin")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public String showListKhachHang(Model model) {
        model.addAttribute("khachHangs", service.findAll());
        return "admin/list";
    }

    // thêm khách hàng mới
    @GetMapping("/them")
    public String showCreateForm(Model model) {
        model.addAttribute("form", new Customer());
        model.addAttribute("isEdit", false);
        return "admin/form";
    }

    @PostMapping("/them")
    public String create(@Valid @ModelAttribute("form") Customer khachHang,
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
            model.addAttribute("error", "Lỗi khi thêm khách hàng: " + e.getMessage());
            model.addAttribute("isEdit", false);
            return "admin/form";
        }
    }

    // sửa thông tin khách hàng
    @GetMapping("/{id}/sua")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Customer kh = service.findById(id).orElse(null);
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
                         @Valid @ModelAttribute("form") Customer khachHang,
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
            model.addAttribute("error", "Lỗi khi cập nhật khách hàng: " + e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("khachHangId", id);
            return "admin/form";
        }
    }

    // đổi trạng thái tài khoản khách hàng
    @PostMapping("/{id}/doi-trang-thai")
    public String doiTrangThai(@PathVariable Long id, RedirectAttributes ra){
        try {
            service.doiTrangThai(id);
            ra.addFlashAttribute("success", "Đã đổi trạng thái tài khoản thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi đổi trạng thái tài khoản: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
