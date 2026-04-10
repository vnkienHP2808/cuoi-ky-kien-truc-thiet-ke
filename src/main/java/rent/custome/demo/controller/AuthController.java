package rent.custome.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import rent.custome.demo.dto.LoginDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

@Controller
public class AuthController {

 private static final Logger log = LoggerFactory.getLogger(KhachHangService.class);

    private final KhachHangService service;

    public AuthController(KhachHangService service) {
        this.service = service;
    }

    @GetMapping("/dang-nhap")
    public String showLogin(HttpSession session){
        if(session.getAttribute("khachHang") != null){
            KhachHang kh =  (KhachHang) session.getAttribute("khachHang");
            return "admin".equals(kh.getRole()) ? "redirect:/admin" : "redirect:/trang-phuc";
        }
        return "auth/login";
    }

    @PostMapping("/dang-nhap")
    public String login(@Valid @ModelAttribute("form") LoginDto form, 
                        BindingResult br, HttpSession session, RedirectAttributes ra){
        
        if(br.hasErrors()) return "auth/login";
        try {
            KhachHang kh = service.login(form.getUsername(), form.getPassword());
            session.setAttribute("khachHang", kh);
            return kh.getRole().equals("admin") ? "redirect:/admin" : "redirect:/trang-phuc";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Đăng nhập thất bại");
            return "redirect:/dang-nhap";
        }
    }

    @PostMapping("/dang-xuat")
    public String logout(HttpSession session){
        session.invalidate();
        log.info("Nguoi dung da dang xuat khoi he thong");
        return "redirect:/dang-nhap";
    }
}
