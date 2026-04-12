package rent.custome.demo.annotation;

import java.lang.annotation.*;

/**
 * Đánh dấu endpoint yêu cầu role cụ thể.
 * Được AuthInterceptor xử lý tập trung — controller không cần check session thủ công.
 *
 * Dùng:
 *   @RequireRole("admin")              → chỉ admin
 *   @RequireRole("customer")           → chỉ customer
 *   @RequireRole({"admin","customer"}) → cả hai role
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    String[] value();
}
