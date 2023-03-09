package cn.tzq0301.opensaschannel.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

@SpringBootConfiguration
public class OpenSasConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    private final OpenSasProperties properties;

    public OpenSasConfig(OpenSasProperties properties) {
        this.properties = properties;
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(properties.getServer().getPort());
    }
}
