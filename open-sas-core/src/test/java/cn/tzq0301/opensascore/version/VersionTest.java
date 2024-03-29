package cn.tzq0301.opensascore.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {

    @Test
    void isStable() {
        assertTrue(Version.STABLE_VERSION.isStable());
        assertTrue(new Version(1, 0, 2).consistentWith(Version.STABLE_VERSION));
    }
}