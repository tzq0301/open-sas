package cn.tzq0301.opensasspringbootstarter.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "open-sas.version", name = "major")
public record Version(int major, int minor, int patch) {
    public Version(@Value("${open-sas.version.major}") int major,
                   @Value("${open-sas.version.minor}") int minor,
                   @Value("${open-sas.version.patch}") int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static Version of(int major, int minor, int patch) {
        return new Version(major, minor, patch);
    }
}
