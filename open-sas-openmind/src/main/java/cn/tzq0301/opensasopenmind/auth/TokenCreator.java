package cn.tzq0301.opensasopenmind.auth;

import cn.tzq0301.opensasopenmind.config.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class TokenCreator {
    private final Algorithm algorithm;

    private final String issuer;

    public TokenCreator(Algorithm algorithm, JwtProperties jwtProperties) {
        this.algorithm = algorithm;
        this.issuer = jwtProperties.getIssuer();
    }

    public String create(final String subject) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .sign(algorithm);
    }

    public String create(final Long id) {
        return create(id.toString());
    }
}
