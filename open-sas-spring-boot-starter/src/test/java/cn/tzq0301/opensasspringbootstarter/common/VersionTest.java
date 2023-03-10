package cn.tzq0301.opensasspringbootstarter.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class VersionTest {

    @Test
    void of() {
        var versionArguments = new int[][]{
                {-1, 0, 0},
                {0, -1, 0},
                {0, 0, -1}
        };
        for (int[] versionArgument : versionArguments) {
            assertThrows(IllegalArgumentException.class, () -> Version.of(versionArgument[0], versionArgument[1], versionArgument[2]));
        }
    }
}