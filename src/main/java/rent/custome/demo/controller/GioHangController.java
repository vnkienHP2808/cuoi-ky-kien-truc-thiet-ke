package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.repository.KhachHangRepository;
import rent.custome.demo.service.GioHangService;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {

    private final GioHangService service;
    private final KhachHangRepository khachHangRepository;

    public GioHangController(GioHangService service,
                              KhachHangRepository khachHangRepository) {
        this.service = service;
        this.khachHangRepository = khachHangRepository;
    }

    @GetMapping
    public String viewCart(@RequestParam Long khachHangId, Model model) {
        KhachHang kh = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id=" + khachHangId));
        model.addAttribute("items", service.getCartItems(khachHangId));
        model.addAttribute("kh", kh);
        return "gio-hang/view";
    }

    @PostMapping("/them/{trangPhucId}")
    public String addToCart(@PathVariable Long trangPhucId,
                            @RequestParam Long khachHangId,
                            RedirectAttributes ra) {
        try {
            service.addToCart(khachHangId, trangPhucId);
            ra.addFlashAttribute("success", "Đã thêm vào giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trang-phuc/" + trangPhucId + "?khachHangId=" + khachHangId;
    }

    @PostMapping("/xoa/{trangPhucId}")
    public String removeFromCart(@PathVariable Long trangPhucId,
                                 @RequestParam Long khachHangId,
                                 RedirectAttributes ra) {
        try {
            service.removeFromCart(khachHangId, trangPhucId);
            ra.addFlashAttribute("success", "Đã xóa khỏi giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gio-hang?khachHangId=" + khachHangId;
    }
}
