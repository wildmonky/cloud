package org.lizhao.cloud.gateway.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Description 计算请求执行时间
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-11 20:27
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Order(0)
@Component
public class RequestIntercept implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        LocalDateTime startTime = LocalDateTime.now();
        Mono<Void> re = chain.filter(exchange);
        String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        log.info("请求路径：" + requestPath + " ，耗时：" + duration.toMillis() + "ms");
        return re;
    }
}
