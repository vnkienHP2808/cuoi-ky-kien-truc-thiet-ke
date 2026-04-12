package rent.custome.demo.state;

import rent.custome.demo.enums.PhieuThueStatus;

/**
 * Factory phục hồi State object từ enum PhieuThueStatus lưu trong DB.
 * Cần thiết vì State object không được persist, chỉ enum được lưu.
 */
public class PhieuThueStateFactory {

    public static PhieuThueState from(PhieuThueStatus status) {
        return switch (status) {
            case CHO_DAT_COC   -> new ChoDatCocState();
            case CHO_XAC_NHAN -> new ChoXacNhanState();
            case DA_HUY       -> new DaHuyState();
        };
    }
}
