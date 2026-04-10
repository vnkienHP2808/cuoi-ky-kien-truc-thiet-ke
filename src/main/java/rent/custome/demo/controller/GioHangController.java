package rent.custome.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.model.Model;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.GioHangService;
import rent.custome.demo.service.KhachHangService;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {
    private static final Logger log = LoggerFactory.getLogger(KhachHangController.class);

    private final GioHangService gioHangService;
    private final KhachHangService khachHangService;

    

    public GioHangController(GioHangService gioHangService, KhachHangService khachHangService) {
        this.gioHangService = gioHangService;
        this.khachHangService = khachHangService;
    }

    @GetMapping("/{id}")
    public String viewCart(@PathVariable Long id, Model model, RedirectAttributes ra){
        KhachHang kh = khachHangService.findById(id).orElse(null);

        return "/gio-hang/view";
    }
}
