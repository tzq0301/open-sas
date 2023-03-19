package cn.tzq0301.opensasopenmind.service.impl;

import cn.tzq0301.opensascore.channel.ChannelMetaInfo;
import cn.tzq0301.opensasopenmind.entity.token.OpenSasToken;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;
import cn.tzq0301.opensasopenmind.manager.ChannelManager;
import cn.tzq0301.opensasopenmind.repository.OpenSasTokenRepository;
import cn.tzq0301.opensasopenmind.repository.UserRepository;
import cn.tzq0301.opensasopenmind.service.ChannelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelManager channelManager;

    private final OpenSasTokenRepository tokenRepository;

    private final UserRepository userRepository;

    public ChannelServiceImpl(ChannelManager channelManager,
                              OpenSasTokenRepository tokenRepository,
                              UserRepository userRepository) {
        this.channelManager = channelManager;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String createToken(Long userId, LocalDateTime expiredAt) throws AccountNotExistException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AccountNotExistException();
        }

        String uuid = UUID.randomUUID().toString();
        OpenSasToken token = new OpenSasToken(userId, uuid, expiredAt);
        return tokenRepository.save(token).getToken();
    }

    @Override
    public boolean isTokenValid(String token) {
        return tokenRepository.existsByToken(token);
    }

    @Override
    public ChannelMetaInfo meta() {
        return channelManager.meta();
    }
}
