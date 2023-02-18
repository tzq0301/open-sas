package cn.tzq0301.opensasmessagesprintbootstarter.core;

public record Version(int major, int minor, int patch) {
    public static Version of(int major, int minor, int patch) {
        return new Version(major, minor, patch);
    }
}
