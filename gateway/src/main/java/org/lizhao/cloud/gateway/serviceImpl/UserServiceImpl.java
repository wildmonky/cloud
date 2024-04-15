package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.security.context.repository.RedisSecurityContextRepository;
import org.lizhao.cloud.gateway.security.event.UserOfflineEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Description 用户服务 查找、下线
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-01 13:03
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class UserServiceImpl implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private RedisSecurityContextRepository repository;

    /**
     * 获取在线用户
     * @return 在线用户
     */
    public Flux<GatewayUser> onlineUser(String token) {
        return repository.onlineUser(token);
    }

    public Mono<Boolean> offline(String token) {
        applicationEventPublisher.publishEvent(new UserOfflineEvent(this));
        return repository.remove(token);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
