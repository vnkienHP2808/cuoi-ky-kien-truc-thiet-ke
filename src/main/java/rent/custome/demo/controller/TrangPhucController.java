package rent.custome.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.entity.TrangPhuc;
import rent.custome.demo.service.TrangPhucService;

@Controller
@RequestMapping("/trang-phuc")
public class TrangPhucController {

    private final TrangPhucService service;

    public TrangPhucController(TrangPhucService service) {
        this.service = service;
    }
    
    @GetMapping
    public String list(Model model, HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null) return "redirect:/dang-nhap";
        else if(kh.getRole().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này!");
        }

        model.addAttribute("trangPhucs", service.findAll());
        model.addAttribute("loais", service.findAllLoai());
        model.addAttribute("khachHang", kh);
        return "trang-phuc/list";
    }

    @GetMapping("/{id}")
    public String detail(HttpSession session, @PathVariable Long id, Model model, RedirectAttributes ra){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null) return "redirect:/dang-nhap";
        else if(kh.getRole().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này!");
        }
        
        TrangPhuc trangPhuc = service.findById(id).orElse(null);
        if (trangPhuc == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm có id: " + id);
            return "redirect:/trang-phuc";
        }
        
        model.addAttribute("tp", trangPhuc);
        model.addAttribute("khachHang", kh);
        return "/trang-phuc/detail";
    }
}
