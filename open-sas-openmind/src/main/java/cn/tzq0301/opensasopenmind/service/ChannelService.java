package cn.tzq0301.opensasopenmind.service;

import cn.tzq0301.opensascore.channel.meta.ChannelMetaInfo;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;

import java.time.LocalDateTime;

public interface ChannelService {
    String createToken(Long userId, LocalDateTime expiredAt) throws AccountNotExistException;

    boolean isTokenValid(String token);

    ChannelMetaInfo meta();
}
