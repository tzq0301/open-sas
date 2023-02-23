package cn.tzq0301.opensasspringbootstarter.net.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

@SpringBootConfiguration
@ConditionalOnProperty(prefix = "open-sas", name = "port")
public class PortConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory>  {
    private final int port;

    public PortConfig(@Value("${open-sas.port}") int port) {
        this.port = port;
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(port);
    }
}
