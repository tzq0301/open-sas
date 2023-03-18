package cn.tzq0301.opensasopenmind.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootConfiguration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String issuer;

    private int expiration;
}
