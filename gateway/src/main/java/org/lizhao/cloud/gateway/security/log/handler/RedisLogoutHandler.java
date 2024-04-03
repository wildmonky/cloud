package org.lizhao.cloud.gateway.security.log.handler;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.cloud.gateway.security.authentication.TokenAuthenticationToken;
import org.lizhao.cloud.gateway.security.context.repository.RedisSecurityContextRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import  org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import reactor.core.publisher.Mono;

/**
 * Description 登出处理器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-27 20:19
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class RedisLogoutHandler implements ServerLogoutHandler {

    private final RedisSecurityContextRepository repository;

    public RedisLogoutHandler(RedisSecurityContextRepository repository) {
        this.repository = repository;
    }

    /**
     * 登出 清除 redis 中的 token
     * @param exchange the exchange
     * @param authentication the {@link Authentication}
     * @return
     */
    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        assert authentication instanceof TokenAuthenticationToken;
        TokenAuthenticationToken authenticationToken = (TokenAuthenticationToken) authentication;
        return Mono.just(authenticationToken.getToken())
                // 删除 AuthCookie
                .flatMap(repository::remove)
                .doOnNext(flag -> {
                    if (flag) {
                        log.info("用户{}登出成功", authentication.getName());
                    } else {
                        log.error("用户{}登出失败", authentication.getName());
                    }
                    // TODO  测试是否能获取SecurityContext
                })
                .then().contextWrite(ReactiveSecurityContextHolder.clearContext()).then();
    }

}
