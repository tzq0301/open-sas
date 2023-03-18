package cn.tzq0301.opensasopenmind.config;

import cn.tzq0301.opensasopenmind.interceptor.AuthInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry
                .addInterceptor(authInterceptor)
                .excludePathPatterns("/user/login", "/user/register");
    }
}
