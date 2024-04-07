package org.lizhao.cloud.gateway.security;

import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Description redirect 跳转策略：
 *          1、XMLHttpRequest下，在响应头中写入跳转路径；
 *          2、其他：redirect跳转
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
        if("XMLHttpRequest".equalsIgnoreCase(header)){
            //如果是XMLHttpRequest请求
            exchange.getResponse().getHeaders().add("redirect-url", location.getPath());
            return Mono.empty();
        } else {
            return super.sendRedirect(exchange, location);
        }
    }

}
