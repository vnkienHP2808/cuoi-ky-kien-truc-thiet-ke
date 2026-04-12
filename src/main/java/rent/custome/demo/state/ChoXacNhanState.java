package rent.custome.demo.state;

import rent.custome.demo.entity.PhieuThue;
import rent.custome.demo.enums.PhieuThueStatus;
import rent.custome.demo.enums.TrangThaiDatCoc;

/**
 * STATE: CHO_XAC_NHAN
 * Khách hàng đã đặt cọc, đang chờ admin xác nhận.
 *
 * Cho phép: xacNhan(), huy()
 * Không cho phép: datCoc() — đã đặt cọc rồi
 */
public class ChoXacNhanState implements PhieuThueState {

    @Override
    public void datCoc(PhieuThue phieuThue) {
        throw new IllegalStateException(
            "Phiếu đã được đặt cọc trước đó"
        );
    }

    @Override
    public void huy(PhieuThue phieuThue) {
        phieuThue.setTrangThai(PhieuThueStatus.DA_HUY);
        // Đã đặt cọc → cần hoàn trả
        phieuThue.setTrangThaiDatCoc(TrangThaiDatCoc.DA_HOAN_TRA);
        phieuThue.applyState(new DaHuyState());
    }

    @Override
    public String getTenTrangThai() {
        return "Chờ xác nhận";
    }
}
