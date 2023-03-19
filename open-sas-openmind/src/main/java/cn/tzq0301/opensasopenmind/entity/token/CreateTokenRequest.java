package cn.tzq0301.opensasopenmind.entity.token;

import java.time.LocalDateTime;

public record CreateTokenRequest(Long userId, LocalDateTime expiredAt) {
}
