package org.lizhao.cloud.gateway.security.csrf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Description 将 Mono<CsrfToken> 挂载到 filter reactor 链中，这样才会被订阅，从而生成CsrfToken
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-03 0:42
 * @since 0.0.1-SNAPSHOT
 * @see SecurityWebFiltersOrder#CSRF 400
 */
@Slf4j
@Order(401)
@Component
public class CsrfTokenSubscribeFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<CsrfToken> csrfTokenMono = exchange.getAttribute(CsrfToken.class.getName());
        csrfTokenMono = Objects.requireNonNullElse(csrfTokenMono, Mono.empty());
        return csrfTokenMono.doOnSuccess(xorToken -> {
            exchange.getAttributes().put("xorCsrfToken", xorToken);
            log.info("xorToken: " + xorToken.getToken());
        }).then(chain.filter(exchange));
    }

}
