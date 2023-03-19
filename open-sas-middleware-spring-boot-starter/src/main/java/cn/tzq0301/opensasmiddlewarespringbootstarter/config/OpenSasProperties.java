package cn.tzq0301.opensasmiddlewarespringbootstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open-sas")
@Data
public class OpenSasProperties {
    private Middleware middleware;

    private OpenMind openMind;

    @Data
    public static class Middleware {
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
    public static class OpenMind {
        private String host;
        private int port;
        private String token;
    }
}
