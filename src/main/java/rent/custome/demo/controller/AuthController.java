package rent.custome.demo.controller;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import rent.custome.demo.dto.LoginDto;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.service.KhachHangService;

@Controller
public class AuthController {
    private final KhachHangService service;

    public AuthController(KhachHangService service) {
        this.service = service;
    }

    @GetMapping("/dang-nhap")
    public String showLogin(HttpSession session){
        if(session.getAttribute("khachHang") != null) return "redirect:/admin";
        return "auth/login";
    }

    @PostMapping("/dang-nhap")
    public String login(@Valid @ModelAttribute("form") LoginDto form, 
                        BindingResult br, HttpSession session, RedirectAttributes ra){
        
        if(br.hasErrors()) return "auth/login";
        try {
            KhachHang kh = service.login(form.getUsername(), form.getPassword());
            session.setAttribute("khachHang", kh);
            return "redirect:/admin";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Đăng nhập thất bại");
            return "redirect:/dang-nhap";
        }
    }

    @PostMapping("/dang-xuat")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/dang-nhap";
    }
}
