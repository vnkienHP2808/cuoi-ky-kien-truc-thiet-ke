package rent.custome.demo.state;

import rent.custome.demo.entity.PhieuThue;

/**
 * STATE: DA_HUY
 * Phiếu đã bị hủy. Không thể thực hiện bất kỳ hành động nào.
 */
public class DaHuyState implements PhieuThueState {

    @Override
    public void datCoc(PhieuThue phieuThue) {
        throw new IllegalStateException(
            "Phiếu đã bị hủy, không thể đặt cọc"
        );
    }

    @Override
    public void huy(PhieuThue phieuThue) {
        throw new IllegalStateException(
            "Phiếu đã bị hủy trước đó"
        );
    }

    @Override
    public String getTenTrangThai() {
        return "Đã hủy";
    }
}
