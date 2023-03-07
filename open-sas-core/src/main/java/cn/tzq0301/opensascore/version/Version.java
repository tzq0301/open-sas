package cn.tzq0301.opensascore.version;

import static com.google.common.base.Preconditions.checkArgument;

public record Version(int major, int minor, int patch) {
    public static final int DEFAULT_MAJOR = 0;

    public static final int DEFAULT_MINOR = 0;

    public static final int DEFAULT_PATCH = 0;

    public static final Version DEFAULT_VERSION = new Version(DEFAULT_MAJOR, DEFAULT_MINOR, DEFAULT_PATCH);

    public Version {
        checkArgument(major >= 0, "major should be not less than 0");
        checkArgument(minor >= 0, "minor should be not less than 0");
        checkArgument(patch >= 0, "patch should be not less than 0");
    }
}
