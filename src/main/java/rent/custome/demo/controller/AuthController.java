package rent.custome.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.dto.LoginDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final KhachHangService service;

    public AuthController(KhachHangService service) {
        this.service = service;
    }

    // Không cần @RequireRole — trang public
    @GetMapping("/dang-nhap")
    public String showLogin(HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh != null) {
            return "admin".equals(kh.getRole()) ? "redirect:/admin" : "redirect:/trang-phuc";
        }
        return "auth/login";
    }

    @PostMapping("/dang-nhap")
    public String login(@Valid @ModelAttribute("form") LoginDto form,
                        BindingResult br, HttpSession session, RedirectAttributes ra) {
        if (br.hasErrors()) return "auth/login";
        try {
            KhachHang kh = service.login(form.getUsername(), form.getPassword());
            session.setAttribute("khachHang", kh);
            log.info("Dang nhap thanh cong: username={}", kh.getUsername());
            return "admin".equals(kh.getRole()) ? "redirect:/admin" : "redirect:/trang-phuc";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
            return "redirect:/dang-nhap";
        }
    }

    @PostMapping("/dang-xuat")
    public String logout(HttpSession session) {
        log.info("Nguoi dung dang xuat");
        session.invalidate();
        return "redirect:/dang-nhap";
    }
}
