package cn.tzq0301.opensasmessagesprintbootstarter.net.common.response;

import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.ResponseEnum.ERROR;
import static cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.ResponseEnum.SUCCESS;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public record Response<T>(int code, @NonNull String message, @Nullable T data) {
    public Response {
        checkNotNull(message);
    }

    public Response(@NonNull ResponseEnum responseEnum, @Nullable T data) {
        this(responseEnum.getCode(), responseEnum.getMessage(), data);
    }

    @CheckReturnValue
    public static Response<?> success() {
        return new Response<>(SUCCESS, null);
    }

    @CheckReturnValue
    public static <T> Response<T> success(@Nullable final T data) {
        return new Response<>(SUCCESS, data);
    }

    @CheckReturnValue
    public static Response<?> error() {
        return new Response<>(ERROR, null);
    }

    @CheckReturnValue
    public static Response<?> error(int code, @NonNull final String message) {
        checkNotNull(message);
        return new Response<>(code, message, null);
    }

    @CheckReturnValue
    public static Response<?> error(@NonNull ResponseEnum responseEnum, @NonNull final String message) {
        checkNotNull(responseEnum);
        checkNotNull(message);
        return new Response<>(responseEnum.getCode(), message, null);
    }

    @CheckReturnValue
    public static Response<?> error(@NonNull final String message) {
        checkNotNull(message);
        return new Response<>(ERROR.getCode(), message, null);
    }

    @CheckReturnValue
    public static <T> Response<T> error(@Nullable final T data) {
        return new Response<>(ERROR, data);
    }

    @CheckReturnValue
    public static <T> Response<T> error(@NonNull ResponseEnum responseEnum) {
        checkNotNull(responseEnum);
        checkArgument(responseEnum.getCode() != SUCCESS.getCode());
        return new Response<>(responseEnum, null);
    }

    @CheckReturnValue
    public static <T> Response<T> error(@NonNull ResponseEnum responseEnum, @Nullable final T data) {
        checkNotNull(responseEnum);
        checkArgument(responseEnum.getCode() != SUCCESS.getCode());
        return new Response<>(responseEnum, data);
    }
}
