package rent.custome.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import rent.custome.demo.dto.CreateOrderRequest;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.repository.TrangPhucRepository;
import rent.custome.demo.service.PhieuThueService;

@Controller
@RequestMapping("/phieu-thue")
public class PhieuThueController {

    private final PhieuThueService service;
    private final TrangPhucRepository trangPhucRepository;

    

    public PhieuThueController(PhieuThueService service, TrangPhucRepository trangPhucRepository) {
        this.service = service;
        this.trangPhucRepository = trangPhucRepository;
    }

    @GetMapping("/tao")
    public String showCreateForm(HttpSession session, Model model, RedirectAttributes ra){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null){
            ra.addFlashAttribute("error", "Vui lòng đăng nhập"); 
            return "redirect:/dang-nhap";
        }
        
        model.addAttribute("kh", kh);
        model.addAttribute("req", new CreateOrderRequest());

        return "phieu-thue/tao";
    }

    @PostMapping("/tao")
    public String create(HttpSession session,
                         @Valid @ModelAttribute("req") CreateOrderRequest req,
                         BindingResult br, Model model, RedirectAttributes ra){

        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null){
            ra.addFlashAttribute("error", "Vui lòng đăng nhập"); 
            return "redirect:/dang-nhap";
        }

        if (br.hasErrors()) { model.addAttribute("kh", kh); return "phieu-thue/tao"; }

        try {
            PhieuThue phieuThue = service.createFromCart(kh.getId(), req);
            ra.addFlashAttribute("success", "Đã tạo phiếu thuê #" + phieuThue.getId());
            return "redirect:/phieu-thue/" + phieuThue.getId(); 

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("kh", kh);
            return "phieu-thue/tao";
        }
    }

    @GetMapping("/{phieuThueId}") 
    public String detail(HttpSession session, @PathVariable Long phieuThueId, Model model, RedirectAttributes ra){

        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null){
            ra.addFlashAttribute("error", "Vui lòng đăng nhập"); 
            return "redirect:/dang-nhap";
        }

        PhieuThue phieuThue = service.findById(phieuThueId).orElse(null);
        
        if(phieuThue == null){
            ra.addFlashAttribute("error", "Không tìm thấy phiếu thuê"); 
            return "redirect:/phieu-thue/cua-toi";
        }

        if (!phieuThue.getKhachHangId().equals(kh.getId()) && !"admin".equals(kh.getRole())) {
            ra.addFlashAttribute("error", "Bạn không có quyền xem phiếu thuê này!"); 
            return "redirect:/phieu-thue/cua-toi";
        }

        model.addAttribute("kh", kh);
        model.addAttribute("pt", phieuThue);
        
        model.addAttribute("trangPhucs", phieuThue.getChiTiet().stream()
                .map(ct -> trangPhucRepository.findById(ct.getTrangPhucId()).orElse(null))
                .toList());

        return "phieu-thue/detail";
    }

    @PostMapping("/{phieuThueId}/dat-coc")
    public String datCoc(HttpSession session, @PathVariable Long phieuThueId, RedirectAttributes ra){
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null){
            ra.addFlashAttribute("error", "Vui lòng đăng nhập"); 
            return "redirect:/dang-nhap";
        }

        PhieuThue phieuThue = service.findById(phieuThueId).orElse(null);
        
        if(phieuThue == null){
            ra.addFlashAttribute("error", "Không tìm thấy phiếu thuê"); 
            return "redirect:/phieu-thue/cua-toi";
        }

        if (!phieuThue.getKhachHangId().equals(kh.getId()) && !"admin".equals(kh.getRole())) {
            ra.addFlashAttribute("error", "Bạn không có xóa phiếu thuê này"); 
            return "redirect:/phieu-thue/cua-toi";
        }

        try {
            service.datCoc(phieuThueId);
            ra.addFlashAttribute("success", "Bạn đã đặt cọc thành công. Vui lòng đến lấy đúng hẹn");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi tiến hành đặt cọc: " + e.getMessage());
        }

        return "redirect:/phieu-thue/" + phieuThueId;
    }

    @PostMapping("/{phieuThueId}/huy")
    public String huyPhieu(HttpSession session, @PathVariable Long phieuThueId, RedirectAttributes ra){
        
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null){
            ra.addFlashAttribute("error", "Vui lòng đăng nhập"); 
            return "redirect:/dang-nhap";
        }

        PhieuThue phieuThue = service.findById(phieuThueId).orElse(null);
        
        if(phieuThue == null){
            ra.addFlashAttribute("error", "Không tìm thấy phiếu thuê"); 
            return "redirect:/phieu-thue/cua-toi";
        }

        if (!phieuThue.getKhachHangId().equals(kh.getId()) && !"admin".equals(kh.getRole())) {
            ra.addFlashAttribute("error", "Bạn không thể hủy phiếu thuê này"); 
            return "redirect:/phieu-thue/cua-toi";
        }

        try {
            service.huyPhieu(phieuThueId);
            ra.addFlashAttribute("success", "Đã hủy phiếu thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi hủy phiếu: " + e.getMessage());
        }

        return "redirect:/phieu-thue/" + phieuThueId;
    }

    @GetMapping("/cua-toi")
    public String showListPhieuThue(HttpSession session, Model model, RedirectAttributes ra){
        
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh == null){
            ra.addFlashAttribute("error", "Vui lòng đăng nhập"); 
            return "redirect:/dang-nhap";
        }

        else if(!"admin".equals(kh.getRole())){
            ra.addFlashAttribute("error", "Bạn không có quyền truy cập vào trang này");
        }

        model.addAttribute("kh", kh);
        model.addAttribute("phieus", service.findByKhachHangId(kh.getId()));

        
        return "phieu-thue/list-by-kh";
    }
}
