package cn.tzq0301.opensasopenmind.interceptor;

import cn.tzq0301.opensasopenmind.auth.AuthUtils;
import cn.tzq0301.opensasopenmind.auth.TokenVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenVerifier verifier;

    public AuthInterceptor(TokenVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        Optional<String> optionalToken = AuthUtils.getToken(request);

        if (optionalToken.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String token = optionalToken.get();

        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}
