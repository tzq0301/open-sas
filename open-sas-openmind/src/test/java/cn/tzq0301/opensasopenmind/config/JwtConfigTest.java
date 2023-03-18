package cn.tzq0301.opensasopenmind.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JwtConfigTest {

    @Test
    void test() throws NoSuchAlgorithmException, InterruptedException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("tzq")
                .build();

        {
            String token = JWT.create()
                    .withExpiresAt(Date.from(Instant.now()).toInstant().plusSeconds(1))
                    .withIssuer("tzq")
                    .sign(algorithm);

            assertNotNull(verifier.verify(token));
            Thread.sleep(1500L);
            assertThrows(JWTVerificationException.class, () -> verifier.verify(token));
        }

        {
            String token = JWT.create()
                    .withExpiresAt(Date.from(Instant.now()).toInstant().plusSeconds(1))
                    .withIssuer("abc")
                    .sign(algorithm);
            assertThrows(JWTVerificationException.class, () -> verifier.verify(token));
        }

        {
            String subject = "10001";
            String token = JWT.create()
                    .withIssuer("tzq")
                    .withSubject(subject)
                    .sign(algorithm);
            assertEquals(subject, verifier.verify(token).getSubject());
        }
    }
}