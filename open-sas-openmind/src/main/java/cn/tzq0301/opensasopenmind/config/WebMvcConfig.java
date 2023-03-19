package cn.tzq0301.opensasopenmind.config;

import cn.tzq0301.opensasopenmind.interceptor.AuthInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    private final ObjectMapper objectMapper;

    public WebMvcConfig(AuthInterceptor authInterceptor, ObjectMapper objectMapper) {
        this.authInterceptor = authInterceptor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry
                .addInterceptor(authInterceptor)
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/channel/serverAddr");
    }

    @Override
    public void configureMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
