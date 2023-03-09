package cn.tzq0301.opensassubscriberspringbootstarter.config;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.SubscriberListenerRegistry;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.version.Version;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class OpenSasConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    private final OpenSasProperties openSasProperties;

    public OpenSasConfig(OpenSasProperties openSasProperties) {
        this.openSasProperties = openSasProperties;
    }

    @Bean
    public Group group() {
        return new Group(openSasProperties.getSubscriber().getGroup());
    }

    @Bean
    public Version version() {
        var propertiedVersion = openSasProperties.getSubscriber().getVersion();
        if (propertiedVersion == null) {
            return Version.DEFAULT_VERSION;
        }
        var major = propertiedVersion.getMajor();
        var minor = propertiedVersion.getMinor();
        var patch = propertiedVersion.getPatch();
        return new Version(major, minor, patch);
    }

    @Bean
    public Priority priority() {
        return new Priority(openSasProperties.getSubscriber().getPriority());
    }

    @Bean
    public SubscriberListenerRegistry subscriberListenerRegistry() {
        return new SubscriberListenerRegistry();
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(openSasProperties.getSubscriber().getPort());
    }
}
