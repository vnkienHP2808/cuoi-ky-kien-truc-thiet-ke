package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import rent.custome.demo.entity.KhachHang;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        KhachHang kh = (KhachHang)session.getAttribute("khachHang");
        if(kh == null) return "redirect:/dang-nhap";
        return "admin".equals(kh.getRole()) ? "redirect:/admin" : "redirect:/trang-phuc";
    }
}
