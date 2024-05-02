package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.model.UserInfo;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.security.context.repository.RedisSecurityContextRepository;
import org.lizhao.cloud.gateway.security.event.UserOfflineEvent;
import org.lizhao.cloud.gateway.security.userdetailsservice.WebClientUserDetailsServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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
public class GatewayServiceImpl implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private RedisSecurityContextRepository repository;
    @Resource
    private WebClientUserDetailsServiceImpl webClientUserDetailsServiceImpl;

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

    public Mono<UserInfo> currentUserDetails() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    GatewayUser gatewayUser = (GatewayUser)authentication.getPrincipal();
                    if (gatewayUser.isAnonymous()) {
                        return Mono.just(gatewayUser);
                    }
                    return webClientUserDetailsServiceImpl.findByUserId(gatewayUser.getId());
                });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
