package rent.custome.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import rent.custome.demo.interceptor.AuthInterceptor;

/**
 * Đăng ký AuthInterceptor vào Spring MVC.
 * Loại trừ các trang public: đăng nhập, static resources.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/dang-nhap",
                    "/dang-xuat",
                    "/css/**", "/js/**", "/images/**", "/favicon.ico"
                );
    }
}
