package rent.custome.demo.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class CreateOrderRequest {

    @NotNull(message = "Ngày hẹn lấy không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayHenLay;

    @NotNull(message = "Ngày hẹn trả không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayHenTra;

    public LocalDate getNgayHenLay() {
        return ngayHenLay;
    }

    public void setNgayHenLay(LocalDate ngayHenLay) {
        this.ngayHenLay = ngayHenLay;
    }

    public LocalDate getNgayHenTra() {
        return ngayHenTra;
    }

    public void setNgayHenTra(LocalDate ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }
}
