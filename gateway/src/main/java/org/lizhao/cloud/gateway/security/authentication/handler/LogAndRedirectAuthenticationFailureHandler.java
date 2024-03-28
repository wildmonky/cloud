package org.lizhao.cloud.gateway.security.authentication.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-26 23:19
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class LogAndRedirectAuthenticationFailureHandler extends RedirectServerAuthenticationFailureHandler {
    /**
     * Creates an instance
     *
     * @param location the location to redirect to (i.e. "/login?failed")
     */
    public LogAndRedirectAuthenticationFailureHandler(String location) {
        super(location);
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.error("用户验证失败", exception);
        return super.onAuthenticationFailure(webFilterExchange, exception);
    }

}
