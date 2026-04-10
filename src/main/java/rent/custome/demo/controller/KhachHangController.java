package rent.custome.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import rent.custome.demo.dto.KhachHangDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/khach-hang")
public class KhachHangController {
    private static final Logger log = LoggerFactory.getLogger(KhachHangController.class);
    
    private final KhachHangService service;

    public KhachHangController(KhachHangService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("khachHangs", service.findAll());
        return "khach-hang/list";
    }

    @GetMapping("/them")
    public String showAddForm(Model model){
        model.addAttribute("form", new KhachHangDto());
        model.addAttribute("isEdit", false);
        return "khach-hang/form";
    }

    @PostMapping("/them")
    public String create(@Valid @ModelAttribute KhachHangDto form, BindingResult br, Model model, RedirectAttributes ra){
        if (br.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "khach-hang/form";
        }

        try {
            service.create(form);
            ra.addFlashAttribute("success", "Tạo mới khách hàng thành công");
        } catch (Exception e) {
            model.addAttribute("isEdit", false);
            model.addAttribute("error", e.getMessage());
            return "khach-hang/form";
        }
        return "redirect:/khach-hang";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id ,Model model, RedirectAttributes ra){
        KhachHang kh = service.findById(id).orElse(null);
        if(kh == null){
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/khach-hang";
        }
        model.addAttribute("kh", kh);
        return "khach-hang/detail";
    }
    

    @GetMapping("/{id}/sua")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra){
        KhachHang kh = service.findById(id).orElse(null);
        if(kh == null){
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/khach-hang";
        }

        KhachHangDto form = new KhachHangDto();
        form.setHoTen(kh.getHoTen());
        form.setUsername(kh.getUsername());
        form.setPassword(kh.getPassword());
        form.setEmail(kh.getEmail());
        form.setSoDienThoai(kh.getSoDienThoai());
        form.setDiaChi(kh.getDiaChi());
        form.setDob(kh.getDob());

        model.addAttribute("form", form);
        model.addAttribute("khachHangId", id);
        model.addAttribute("isEdit", true);

        return "khach-hang/form";
    }

    @PostMapping("/{id}/sua")
    public String update(@Valid @ModelAttribute KhachHangDto form, BindingResult br, @PathVariable Long id, Model model, RedirectAttributes ra){
        if (br.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "khach-hang/form";
        }

        try {
            service.update(id, form);
            ra.addFlashAttribute("success", "Cập nhập thông tin khách hàng id={}'" + id + "'thành công");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("khachHangId", id);
            return "khach-hang/form";
        }
        
        return "redirect:/khach-hang";
    }

    @PostMapping("/{id}/xoa")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes ra){
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Đã xóa khách hàng thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/khach-hang";
    }

    
}
