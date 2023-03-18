package cn.tzq0301.opensasopenmind.auth;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class TokenVerifier {
    private final JWTVerifier verifier;

    public TokenVerifier(JWTVerifier verifier) {
        this.verifier = verifier;
    }

    public DecodedJWT verify(final String token) throws JWTVerificationException {
        return verifier.verify(token);
    }
}
