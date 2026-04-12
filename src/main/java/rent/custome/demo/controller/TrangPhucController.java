package rent.custome.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.annotation.RequireRole;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.service.TrangPhucService;

@Controller
@RequestMapping("/trang-phuc")
@RequireRole("customer")  // Danh sách trang phục chỉ dành cho customer
public class TrangPhucController {

    private final TrangPhucService service;

    public TrangPhucController(TrangPhucService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        model.addAttribute("trangPhucs", service.findAll());
        model.addAttribute("loais", service.findAllLoai());
        model.addAttribute("khachHang", kh);
        return "trang-phuc/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         RedirectAttributes ra, HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        TrangPhuc tp = service.findById(id).orElse(null);
        if (tp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm id=" + id);
            return "redirect:/trang-phuc";
        }
        model.addAttribute("tp", tp);
        model.addAttribute("khachHang", kh);
        return "trang-phuc/detail";
    }
}
