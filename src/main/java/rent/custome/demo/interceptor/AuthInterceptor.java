package rent.custome.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import rent.custome.demo.annotation.RequireRole;
import rent.custome.demo.entity.KhachHang;

import java.util.Arrays;

/**
 * Interceptor xử lý phân quyền TẬP TRUNG cho toàn bộ ứng dụng.
 *
 * Thay vì mỗi controller tự kiểm tra session + role (lằng nhằng, dễ bỏ sót),
 * interceptor này tự động:
 *   1. Kiểm tra đã đăng nhập chưa (nếu endpoint có @RequireRole)
 *   2. Kiểm tra role có khớp không
 *   3. Redirect hoặc trả 403 nếu không đủ quyền
 *
 * Controller chỉ cần annotate @RequireRole("admin") hoặc @RequireRole("customer").
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod method)) return true;

        // Lấy @RequireRole: ưu tiên method-level, fallback class-level
        RequireRole requireRole = method.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = method.getBeanType().getAnnotation(RequireRole.class);
        }

        // Endpoint không yêu cầu role → cho qua
        if (requireRole == null) return true;

        KhachHang kh = (KhachHang) request.getSession().getAttribute("khachHang");

        // Chưa đăng nhập
        if (kh == null) {
            response.sendRedirect(request.getContextPath() + "/dang-nhap");
            return false;
        }

        // Kiểm tra role
        String[] allowedRoles = requireRole.value();
        boolean hasRole = Arrays.asList(allowedRoles).contains(kh.getRole());

        if (!hasRole) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập trang này");
            return false;
        }

        return true;
    }
}
