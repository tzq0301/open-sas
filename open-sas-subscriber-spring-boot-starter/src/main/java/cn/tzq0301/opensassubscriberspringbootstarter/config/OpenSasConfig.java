package cn.tzq0301.opensassubscriberspringbootstarter.config;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.SubscriberListenerRegistry;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.version.Version;
import lombok.Getter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@Getter
public class OpenSasConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    private final int port;

    private final Group group;

    private final Version version;

    private final Priority priority;

    public OpenSasConfig(final OpenSasProperties openSasProperties) {
        this.port = openSasProperties.getPort();
        this.group = getGroup(openSasProperties);
        this.version = getVersion(openSasProperties);
        this.priority = getPriority(openSasProperties);
    }

    private Group getGroup(final OpenSasProperties openSasProperties) {
        return new Group(openSasProperties.getSubscriber().getGroup());
    }

    private Version getVersion(final OpenSasProperties openSasProperties) {
        var propertiedVersion = openSasProperties.getSubscriber().getVersion();
        if (propertiedVersion == null) {
            return Version.DEFAULT_VERSION;
        }
        var major = propertiedVersion.getMajor();
        var minor = propertiedVersion.getMinor();
        var patch = propertiedVersion.getPatch();
        return new Version(major, minor, patch);
    }

    private Priority getPriority(final OpenSasProperties openSasProperties) {
        return new Priority(openSasProperties.getSubscriber().getPriority());
    }

    @Bean
    public SubscriberListenerRegistry subscriberListenerRegistry() {
        return new SubscriberListenerRegistry();
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(port);
    }
}
