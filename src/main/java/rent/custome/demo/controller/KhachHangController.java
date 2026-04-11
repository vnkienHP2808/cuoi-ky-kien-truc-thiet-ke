package rent.custome.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import rent.custome.demo.dto.KhachHangDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/admin")
public class KhachHangController {
    private final KhachHangService service;

    public KhachHangController(KhachHangService service) {
        this.service = service;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if(kh == null){
            return "redirect:/dang-nhap";
        }

        if(!"admin".equals(kh.getRole())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang quản trị!");
        }
        model.addAttribute("khachHangs", service.findAll());
        return "admin/list";
    }

    @GetMapping("/them")
    public String showAddForm(HttpSession session, Model model){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if(kh == null){
            return "redirect:/dang-nhap";
        }

        if(!"admin".equals(kh.getRole())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang quản trị!");
        }
        model.addAttribute("form", new KhachHangDto());
        model.addAttribute("isEdit", false);
        return "admin/form";
    }

    @PostMapping("/them")
    public String create(@Valid @ModelAttribute("form") KhachHangDto form, BindingResult br, Model model, RedirectAttributes ra){
        if (br.hasErrors()) {
            model.addAttribute("form", form);   
            model.addAttribute("isEdit", false);
            return "admin/form";
        }

        try {
            service.create(form);
            ra.addFlashAttribute("success", "Tạo mới khách hàng thành công");
        } catch (Exception e) {
            model.addAttribute("form", form);
            model.addAttribute("isEdit", false);
            model.addAttribute("error", e.getMessage());
            return "admin/form";
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id ,Model model, RedirectAttributes ra, HttpSession session){
        KhachHang currentKh = (KhachHang) session.getAttribute("khachHang");
        if(currentKh == null){
            return "redirect:/dang-nhap";
        }

        if(!"admin".equals(currentKh.getRole())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang quản trị!");
        }

        KhachHang kh = service.findById(id).orElse(null);
        if(kh == null){
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin";
        }
        model.addAttribute("kh", kh);
        return "admin/detail";
    }
    

    @GetMapping("/{id}/sua")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra, HttpSession session){
        KhachHang currentKh = (KhachHang) session.getAttribute("khachHang");
        if(currentKh == null){
            return "redirect:/dang-nhap";
        }

        if(!"admin".equals(currentKh.getRole())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang quản trị!");
        }

        KhachHang kh = service.findById(id).orElse(null);
        if(kh == null){
            ra.addFlashAttribute("error", "Không tìm thấy khách hàng");
            return "redirect:/admin";
        }

        KhachHangDto form = new KhachHangDto();
        form.setHoTen(kh.getHoTen());
        form.setUsername(kh.getUsername());
        form.setPassword(kh.getPassword());
        form.setEmail(kh.getEmail());
        form.setSoDienThoai(kh.getSoDienThoai());
        form.setDiaChi(kh.getDiaChi());
        form.setDob(kh.getDob());
        form.setRole(kh.getRole());
        form.setIsActive(kh.getIsActive());

        model.addAttribute("form", form);
        model.addAttribute("khachHangId", id);
        model.addAttribute("isEdit", true);

        return "admin/form";
    }

    @PostMapping("/{id}/sua")
    public String update(@Valid @ModelAttribute("form") KhachHangDto form, BindingResult br, @PathVariable Long id, Model model, RedirectAttributes ra){
        if (br.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("khachHangId", id);
            model.addAttribute("isEdit", true);
            return "admin/form";
        }

        try {
            service.update(id, form);
            ra.addFlashAttribute("success", "Cập nhập thông tin khách hàng " + id + " thành công");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("form", form);   
            model.addAttribute("isEdit", true);
            model.addAttribute("khachHangId", id);
            return "admin/form";
        }
        
        return "redirect:/admin";
    }

    @PostMapping("/{id}/doi-trang-thai")
    public String toggleTrangThai(@PathVariable Long id, RedirectAttributes ra){
        try {
            service.toggleTrangThai(id);
            ra.addFlashAttribute("success", "Đã đổi trạng thái tài khoản thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
