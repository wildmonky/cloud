package org.lizhao.cloud.gateway.security.log.handler;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.cloud.gateway.security.security.context.RedisSecurityContextImpl;
import org.lizhao.cloud.gateway.security.security.context.repository.RedisSecurityContextRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import  org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-27 20:19
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class RedisLogoutHandler implements ServerLogoutHandler {

    @Resource
    private RedisSecurityContextRepository repository;

    /**
     * 登出 清除 redis 中的 token
     * @param exchange the exchange
     * @param authentication the {@link Authentication}
     * @return
     */
    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new Throwable("securityContext为空")))
                .map(securityContext -> {
                    assert securityContext instanceof RedisSecurityContextImpl;
                    return ((RedisSecurityContextImpl) securityContext).getToken();
                }).flatMap(token -> repository.remove(token))
                .flatMap(flag -> {
                    if (!flag) {
                        log.info("用户{}登出失败", authentication.getName());
                    }
                    return Mono.empty();
                });
    }

}
