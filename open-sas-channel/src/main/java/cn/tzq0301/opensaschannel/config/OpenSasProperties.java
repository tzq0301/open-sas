package cn.tzq0301.opensaschannel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open-sas")
@Data
public class OpenSasProperties {
    private Server server;

    @Data
    public static class Server {
        private int port;
    }
}
