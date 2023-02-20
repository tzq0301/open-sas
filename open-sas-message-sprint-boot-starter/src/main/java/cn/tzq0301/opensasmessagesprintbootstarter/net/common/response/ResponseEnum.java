package cn.tzq0301.opensasmessagesprintbootstarter.net.common.response;

import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public enum ResponseEnum {
    SUCCESS(0, "Success"),
    ERROR(1, "Error"),
    ILLEGAL_ARGUMENT(2, "IllegalArgument"),
    SERVER_ERROR(3, "ServerError");

    private final int code;

    private final String message;

    ResponseEnum(int code, @NonNull String message) {
        checkNotNull(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
