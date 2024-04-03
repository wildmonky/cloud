package org.lizhao.cloud.gateway.security;

import org.springframework.core.log.LogMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-03 23:28
 * @since 0.0.1-SNAPSHOT
 */
public class XMLHttpRequestRedirectStrategy extends DefaultServerRedirectStrategy {
    @Override
    public Mono<Void> sendRedirect(ServerWebExchange exchange, URI location) {

        String header = exchange.getRequest().getHeaders().getFirst("X-Requested-With");
        if("XMLHttpRequest".equals(header)){
            //如果是XMLHttpRequest请求
            exchange.getResponse().getHeaders().add("redirect-url", location.getPath());
            return Mono.empty();
        } else {
            return super.sendRedirect(exchange, location);
        }
    }

}
