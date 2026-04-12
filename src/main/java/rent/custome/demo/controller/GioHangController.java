package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.config.AppConfig;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.repository.KhachHangRepository;
import rent.custome.demo.service.GioHangService;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {

    private final GioHangService service;
    private final KhachHangRepository khachHangRepository;
    private final AppConfig appConfig;

    public GioHangController(GioHangService service,
                              KhachHangRepository khachHangRepository,
                              AppConfig appConfig) {
        this.service = service;
        this.khachHangRepository = khachHangRepository;
        this.appConfig = appConfig;
    }

    @GetMapping
    public String viewCart(Model model) {
        Long khachHangId = appConfig.getKhachHangId();
        KhachHang kh = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id=" + khachHangId));
        model.addAttribute("items", service.getCartItems(khachHangId));
        model.addAttribute("kh", kh);
        model.addAttribute("selectedKhachHangId", khachHangId);
        return "gio-hang/view";
    }

    @PostMapping("/them/{trangPhucId}")
    public String addToCart(@PathVariable Long trangPhucId, RedirectAttributes ra) {
        Long khachHangId = appConfig.getKhachHangId();
        try {
            service.addToCart(khachHangId, trangPhucId);
            ra.addFlashAttribute("success", "Đã thêm vào giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trang-phuc/" + trangPhucId;
    }

    @PostMapping("/xoa/{trangPhucId}")
    public String removeFromCart(@PathVariable Long trangPhucId, RedirectAttributes ra) {
        Long khachHangId = appConfig.getKhachHangId();
        try {
            service.removeFromCart(khachHangId, trangPhucId);
            ra.addFlashAttribute("success", "Đã xóa khỏi giỏ hàng");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/gio-hang";
    }
}
