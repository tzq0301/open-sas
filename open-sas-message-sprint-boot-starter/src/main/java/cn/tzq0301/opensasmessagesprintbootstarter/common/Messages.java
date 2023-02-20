package cn.tzq0301.opensasmessagesprintbootstarter.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Messages {
    private Messages() {
    }

    public static String toJSON(@NonNull Message message) throws JsonProcessingException {
        checkNotNull(message);
        ObjectMapper objectMapper = new ObjectMapper();
        return toJSON(message, objectMapper);
    }

    public static String toJSON(@NonNull Message message, @NonNull ObjectMapper objectMapper) throws JsonProcessingException {
        checkNotNull(message);
        checkNotNull(objectMapper);
        return objectMapper.writeValueAsString(message);
    }

    public static Message fromJSON(@NonNull String message) throws JsonProcessingException {
        checkNotNull(message);
        ObjectMapper objectMapper = new ObjectMapper();
        return fromJSON(message, objectMapper);
    }

    public static Message fromJSON(@NonNull String message, @NonNull ObjectMapper objectMapper) throws JsonProcessingException {
        checkNotNull(message);
        checkNotNull(objectMapper);
        return objectMapper.readValue(message, Message.class);
    }
}
