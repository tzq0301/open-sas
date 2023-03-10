package cn.tzq0301.opensasmiddlewarespringbootstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open-sas")
@Data
public class OpenSasProperties {
    private Subscriber subscriber;

    private ServerAddr serverAddr;

    @Data
    public static class Subscriber {
        private int port;
        private String group;
        private Version version;
        private int priority;

        @Data
        public static class Version {
            private int major;
            private int minor;
            private int patch;
        }
    }

    @Data
    public static class ServerAddr {
        private String host;
        private int port;
    }
}
