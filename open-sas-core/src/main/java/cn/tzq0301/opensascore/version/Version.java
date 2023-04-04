package cn.tzq0301.opensascore.version;

import com.google.common.collect.ComparisonChain;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public record Version(int major, int minor, int patch) implements Comparable<Version> {
    public static final int DEFAULT_MAJOR = 0;

    public static final int DEFAULT_MINOR = 0;

    public static final int DEFAULT_PATCH = 0;

    public static final Version DEFAULT_VERSION = new Version(DEFAULT_MAJOR, DEFAULT_MINOR, DEFAULT_PATCH);

    public static final int STABLE_MAJOR = 1;

    public static final int STABLE_MINOR = 0;

    public static final int STABLE_PATCH = 0;

    public static final Version STABLE_VERSION = new Version(STABLE_MAJOR, STABLE_MINOR, STABLE_PATCH);

    public Version {
        checkArgument(major >= 0, "major should be not less than 0");
        checkArgument(minor >= 0, "minor should be not less than 0");
        checkArgument(patch >= 0, "patch should be not less than 0");
    }

    public boolean consistentWith(@NonNull Version o) {
        checkNotNull(o);
        if (this.isNotStable() || o.isNotStable()) {
            return false;
        }
        return this.major == o.major;
    }

    public boolean isStable() {
        return this.compareTo(STABLE_VERSION) >= 0;
    }

    public boolean isNotStable() {
        return !isStable();
    }

    @Override
    public int compareTo(@NonNull Version o) {
        return ComparisonChain.start()
                .compare(this.major, o.major)
                .compare(this.minor, o.minor)
                .compare(this.patch, o.patch)
                .result();
    }

    public String format() {
        return String.format("%s.%s.%s", major, minor, patch);
    }
}
