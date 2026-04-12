package rent.custome.demo.state;

import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.enums.PhieuThueStatus;
import rent.custome.demo.enums.TrangThaiDatCoc;

/**
 * STATE: CHO_XU_LY
 * Trạng thái ban đầu ngay sau khi khách hàng tạo phiếu từ giỏ hàng.
 *
 * Cho phép: datCoc(), huy()
 * Không cho phép: xacNhan() — admin chưa thể xác nhận khi chưa có cọc
 */
public class ChoDatCocState implements PhieuThueState {

    @Override
    public void datCoc(PhieuThue phieuThue) {
        phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.DA_THANH_TOAN);
        phieuThue.setTrangThai(PhieuThueStatus.CHO_XAC_NHAN);
        // Chuyển context sang state mới
        phieuThue.applyState(new ChoXacNhanState());
    }

    @Override
    public void huy(PhieuThue phieuThue) {
        phieuThue.setTrangThai(PhieuThueStatus.DA_HUY);
        // Cọc chưa thanh toán → không cần hoàn trả
        phieuThue.applyState(new DaHuyState());
    }

    @Override
    public String getTenTrangThai() {
        return "Chờ xử lý";
    }
}
