package cn.tzq0301.opensasspringbootstarter.util;

public final class MetaUtil {
    private MetaUtil() {}

    public static int getLineNumber() {
        return StackWalker.getInstance(StackWalker.Option.SHOW_HIDDEN_FRAMES)
                .walk((s) -> s.skip(1).findFirst())
                .map(StackWalker.StackFrame::getLineNumber)
                .orElseThrow();
    }
}
