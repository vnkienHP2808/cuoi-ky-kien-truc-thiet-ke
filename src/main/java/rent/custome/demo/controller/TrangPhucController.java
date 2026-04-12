package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.config.AppConfig;
import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.service.TrangPhucService;

@Controller
@RequestMapping("/trang-phuc")
public class TrangPhucController {

    private final TrangPhucService service;
    private final AppConfig appConfig;

    public TrangPhucController(TrangPhucService service, AppConfig appConfig) {
        this.service = service;
        this.appConfig = appConfig;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("trangPhucs", service.findAll());
        model.addAttribute("loais", service.findAllLoai());
        model.addAttribute("selectedKhachHangId", appConfig.getKhachHangId());
        return "trang-phuc/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        TrangPhuc tp = service.findById(id).orElse(null);
        if (tp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm id=" + id);
            return "redirect:/trang-phuc";
        }
        model.addAttribute("tp", tp);
        model.addAttribute("selectedKhachHangId", appConfig.getKhachHangId());
        return "trang-phuc/detail";
    }
}
