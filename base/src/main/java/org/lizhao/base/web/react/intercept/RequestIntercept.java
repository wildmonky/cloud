package org.lizhao.base.web.react.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description 全局请求拦截器
 *             1、计算请求执行时间;
 *             2、解析Token TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-11 20:27
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Order(0)
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class RequestIntercept implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        AtomicReference<LocalDateTime> startTime = new AtomicReference<>(LocalDateTime.now());
        return  chain.filter(exchange)
                .doOnRequest( e -> startTime.set(LocalDateTime.now()))
                .doOnError(Throwable::printStackTrace)
                .doFinally( e -> {
                    String requestStatus = switch (e) {
                        case ON_COMPLETE -> "成功";
                        case ON_ERROR -> "一个错误发生了";
                        default -> "未成功";
                    };
                    String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
                    Duration duration = Duration.between(startTime.get(), LocalDateTime.now());
                    log.info("请求结果：{}，请求路径：{} ，耗时：{} ms", requestStatus, requestPath, duration.toMillis());
        });
    }
}
