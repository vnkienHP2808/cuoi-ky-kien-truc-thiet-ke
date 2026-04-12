package rent.custome.demo.state;

import rent.custome.demo.entity.PhieuThue;

/**
 * STATE PATTERN - Interface định nghĩa các hành động có thể thực hiện
 * trên PhieuThue tùy theo trạng thái hiện tại.
 *
 * Mỗi trạng thái sẽ tự quyết định hành động nào được phép và
 * chuyển sang trạng thái nào tiếp theo.
 */
public interface PhieuThueState {

    /**
     * Khách hàng xác nhận đặt cọc
     */
    void datCoc(PhieuThue phieuThue);


    /**
     * Hủy phiếu thuê (khách hàng hoặc admin)
     */
    void huy(PhieuThue phieuThue);

    /**
     * Tên trạng thái để hiển thị
     */
    String getTenTrangThai();
}
