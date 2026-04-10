package rent.custome.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.GioHangService;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {
    private final GioHangService service;

    public GioHangController(GioHangService service) {
        this.service = service;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null) return "redirect:/dang-nhap";
        else if(kh.getRole().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này!");
        }

        model.addAttribute("items", service.getCartItems(kh.getId()));
        model.addAttribute("kh", kh);

        return "gio-hang/view";
    }

    @PostMapping("/them/{trangPhucId}")
    public String addToCart(@PathVariable Long trangPhucId, Model model, RedirectAttributes ra, HttpSession session){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null) return "redirect:/dang-nhap";

        try {
            service.addToCart(kh.getId(), trangPhucId);
            ra.addFlashAttribute("success", "Đã thêm vào giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi thêm sản phẩm vào giỏ hàng" + e.getMessage());
        }
        return "redirect:/trang-phuc/" + trangPhucId;
    }

    @PostMapping("/xoa/{trangPhucId}")
    public String removeFormCart(@PathVariable Long trangPhucId, Model model, RedirectAttributes ra, HttpSession session){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null) return "redirect:/dang-nhap";

        try {
            service.removeFromCart(kh.getId(), trangPhucId);
            ra.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi xóa sản phẩm khỏi giỏ hàng" + e.getMessage());
        }
        return "redirect:/gio-hang";
    }
}
