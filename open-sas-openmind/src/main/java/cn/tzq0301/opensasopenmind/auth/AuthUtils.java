package cn.tzq0301.opensasopenmind.auth;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public final class AuthUtils {
    public static final String BEARER_PREFIX = "Bearer ";

    private AuthUtils() {}

    public static Optional<String> getToken(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(authorizationHeader)) {
            return Optional.empty();
        }

        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        return Optional.of(token);
    }
}
