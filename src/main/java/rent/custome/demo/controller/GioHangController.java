package rent.custome.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.annotation.RequireRole;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.GioHangService;

@Controller
@RequestMapping("/gio-hang")
@RequireRole("customer")  // Giỏ hàng chỉ dành cho customer
public class GioHangController {

    private final GioHangService service;

    public GioHangController(GioHangService service) {
        this.service = service;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        model.addAttribute("items", service.getCartItems(kh.getId()));
        model.addAttribute("kh", kh);
        return "gio-hang/view";
    }

    @PostMapping("/them/{trangPhucId}")
    public String addToCart(@PathVariable Long trangPhucId,
                            RedirectAttributes ra, HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        try {
            service.addToCart(kh.getId(), trangPhucId);
            ra.addFlashAttribute("success", "Đã thêm vào giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trang-phuc/" + trangPhucId;
    }

    @PostMapping("/xoa/{trangPhucId}")
    public String removeFromCart(@PathVariable Long trangPhucId,
                                 RedirectAttributes ra, HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        try {
            service.removeFromCart(kh.getId(), trangPhucId);
            ra.addFlashAttribute("success", "Đã xóa khỏi giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gio-hang";
    }
}
