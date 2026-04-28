package rent.custome.demo.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rent.custome.demo.entity.KhachHang;
import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.entity.ChiTietPhieuThue;
import rent.custome.demo.repository.KhachHangRepository;
import rent.custome.demo.repository.ChiTietPhieuThueRepository;
import rent.custome.demo.service.PhieuThueService;
import rent.custome.demo.service.TrangPhucService;

import java.util.List;

@Controller
@RequestMapping("/phieu-thue")
public class PhieuThueController {

    private final PhieuThueService service;
    private final TrangPhucService trangPhucService;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietPhieuThueRepository rentalItemRepository;

    public PhieuThueController(PhieuThueService service, TrangPhucService trangPhucService,
            KhachHangRepository khachHangRepository,
            ChiTietPhieuThueRepository rentalItemRepository) {
        this.service = service;
        this.trangPhucService = trangPhucService;
        this.khachHangRepository = khachHangRepository;
        this.rentalItemRepository = rentalItemRepository;
    }

    // tạo phiếu thuê mới từ giỏ hàng của khách hàng
    @GetMapping("/tao")
    public String showCreateForm(@RequestParam Long khachHangId, Model model) {
        KhachHang kh = findKhOrThrow(khachHangId);
        model.addAttribute("kh", kh);
        model.addAttribute("pt", new PhieuThue());
        return "phieu-thue/tao";
    }

    @PostMapping("/tao")
    public String create(@RequestParam Long khachHangId,
                         @Valid @ModelAttribute("pt") PhieuThue pt,
                         BindingResult br, Model model, RedirectAttributes ra) {
        KhachHang kh = findKhOrThrow(khachHangId);
        if (br.hasErrors()) {
            model.addAttribute("kh", kh);
            return "phieu-thue/tao";
        }
        try {
            PhieuThue saved = service.createFromCart(khachHangId, pt);
            ra.addFlashAttribute("success", "Đã tạo phiếu thuê #" + saved.getId());
            return "redirect:/phieu-thue/" + saved.getId() + "?khachHangId=" + khachHangId;
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tạo phiếu thuê: " + e.getMessage());
            model.addAttribute("kh", kh);
            return "phieu-thue/tao";
        }
    }

    // danh sách phiếu thuê của khách hàng
    @GetMapping("/cua-toi")
    public String showListPhieuThue(@RequestParam Long khachHangId, Model model) {
        KhachHang kh = findKhOrThrow(khachHangId);
        model.addAttribute("kh", kh);
        model.addAttribute("phieus", service.findByKhachHangId(khachHangId));
        return "phieu-thue/list-by-kh";
    }

    // đặt cọc phiếu thuê
    @PostMapping("/{id}/dat-coc")
    public String datCoc(@PathVariable Long id,
                         @RequestParam Long khachHangId,
                         RedirectAttributes ra) {
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu");
            return "redirect:/phieu-thue/cua-toi?khachHangId=" + khachHangId;
        }
        if (!pt.getKhachHangId().equals(khachHangId)) {
            ra.addFlashAttribute("error", "Phiếu này không thuộc khách hàng đang chọn");
            return "redirect:/phieu-thue/cua-toi?khachHangId=" + khachHangId;
        }
        
        try {
            service.datCoc(id);
            ra.addFlashAttribute("success", "Đặt cọc thành công. Vui lòng đến lấy đúng hẹn");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi đặt cọc: " + e.getMessage());
        }
        return "redirect:/phieu-thue/" + id + "?khachHangId=" + khachHangId;
    }

    // hủy phiếu thuê
    @PostMapping("/{id}/huy")
    public String huyPhieu(@PathVariable Long id,
                           @RequestParam Long khachHangId,
                           RedirectAttributes ra) {
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu");
            return "redirect:/phieu-thue/cua-toi?khachHangId=" + khachHangId;
        }
        if (!pt.getKhachHangId().equals(khachHangId)) {
            ra.addFlashAttribute("error", "Phiếu này không thuộc khách hàng đang chọn");
            return "redirect:/phieu-thue/cua-toi?khachHangId=" + khachHangId;
        }
        try {
            service.huyPhieu(id);
            ra.addFlashAttribute("success", "Đã hủy phiếu thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi hủy phiếu: " + e.getMessage());
        }
        return "redirect:/phieu-thue/" + id + "?khachHangId=" + khachHangId;
    }

    // xem chi tiết phiếu thuê
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id,
                         @RequestParam Long khachHangId,
                         Model model, RedirectAttributes ra) {
        PhieuThue pt = service.findById(id).orElse(null);
        if (pt == null) {
            ra.addFlashAttribute("error", "Không tìm thấy phiếu thuê");
            return "redirect:/phieu-thue/cua-toi?khachHangId=" + khachHangId;
        }
        if (!pt.getKhachHangId().equals(khachHangId)) {
            ra.addFlashAttribute("error", "Phiếu này không thuộc khách hàng đang chọn");
            return "redirect:/phieu-thue/cua-toi?khachHangId=" + khachHangId;
        }
        KhachHang kh = findKhOrThrow(khachHangId);


        List<ChiTietPhieuThue> chiTiets = rentalItemRepository.findByPhieuThueId(id);

        model.addAttribute("kh", kh);
        model.addAttribute("pt", pt);
        model.addAttribute("trangPhucs", chiTiets.stream()
                .map(ct -> trangPhucService.findById(ct.getTrangPhucId()).orElse(null))
                .toList());
        return "phieu-thue/detail";
    }


    private KhachHang findKhOrThrow(Long id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id=" + id));
    }
}
