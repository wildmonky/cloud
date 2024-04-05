package org.lizhao.cloud.gateway.security.log.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * Description 登出成功处理器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-27 22:16
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class RedisLogoutSuccessHandler extends RedirectServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.info("用户{}登出成功", authentication.getName());
        return super.onLogoutSuccess(exchange, authentication);
    }

}
