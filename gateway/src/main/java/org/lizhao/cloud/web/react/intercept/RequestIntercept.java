package org.lizhao.cloud.web.react.intercept;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.model.UserHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        AtomicReference<LocalDateTime> startTime = new AtomicReference<>(LocalDateTime.now());

        ServerHttpRequest request = exchange.getRequest();
        RequestPath path = request.getPath();

        Mono<Void> result;
        if (StringUtils.isNotBlank(contextPath) & path.value().startsWith(contextPath)) {
            result = chain.filter(
                    exchange.mutate()
                            .request(request.mutate().contextPath(contextPath).build())
                            .build());
        } else {
            result = chain.filter(exchange);
        }
        return result
//                .doFirst(() -> UserHolder.setCurrentUser(new GatewayUser("1", "1", Collections.emptySet()))) // 内置测试用户
//                .doOnRequest(e -> startTime.set(LocalDateTime.now()))
                .doOnError(throwable -> {
                    String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
                    log.info("请求结果: 失败, 请求路径: {}, 耗时: {} ms", requestPath, Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
                    log.error("请求异常", throwable);
                }).doOnSuccess(e -> {
//                    String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
//                    GatewayUser currentUser = (GatewayUser)UserHolder.getCurrentUser();
//                    log.info("请求结果: 成功, 请求路径: {}, 用户: {}({}), 耗时: {} ms", requestPath, currentUser.getUsername(), currentUser.getPhone(), Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
                }).doFinally(e -> {
                    // 请求处理完成移除用户信息
                    UserHolder.removeCurrentUser();
                });
    }
}
