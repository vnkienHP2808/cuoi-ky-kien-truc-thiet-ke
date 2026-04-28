package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.service.TrangPhucService;

@Controller
@RequestMapping("/trang-phuc")
public class TrangPhucController {

    private final TrangPhucService service;

    public TrangPhucController(TrangPhucService service) {
        this.service = service;
    }

    // xem danh sách trang phục
    @GetMapping
    public String showList(Model model) {
        model.addAttribute("trangPhucs", service.findAll());
        return "trang-phuc/list";
    }

    // xem chi tiết trang phục
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id,
                         Model model, RedirectAttributes ra) {
        TrangPhuc tp = service.findById(id).orElse(null);
        if (tp == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm id=" + id);
            return "redirect:/trang-phuc";
        }
        model.addAttribute("tp", tp);
        return "trang-phuc/detail";
    }
}
