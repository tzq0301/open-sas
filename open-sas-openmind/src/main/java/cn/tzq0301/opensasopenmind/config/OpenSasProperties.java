package cn.tzq0301.opensasopenmind.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootConfiguration
@ConfigurationProperties(prefix = "open-sas")
@Data
public class OpenSasProperties {
    private String serverAddr;
}
