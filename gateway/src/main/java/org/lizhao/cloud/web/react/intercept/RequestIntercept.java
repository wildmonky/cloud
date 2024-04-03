package org.lizhao.cloud.web.react.intercept;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.cloud.gateway.configurer.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;
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
 *             计算请求执行时间;
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-11 20:27
 * @since 0.0.1-SNAPSHOT
 * @see SecurityWebFiltersOrder
 * @see Ordered
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE) // SecurityWebFiltersOrder.FIRST
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class RequestIntercept implements WebFilter {

    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    @Resource
    private SecurityProperties securityProperties;

    /**
     * Description
     * {@link CsrfWebFilter#filter} 登录时，设置 Attribute name CsrfToken.class.getName()
     *
     * @since 0.0.1-SNAPSHOT
     * @author lizhao
     * @date 2024/4/2 21:27
     * @param exchange exchange
     * @param chain chain
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @see CookieServerCsrfTokenRepository#generateToken 未订阅，不会生产
     * @see WebSessionServerCsrfTokenRepository#generateToken 未订阅，不会生产
     */
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
                    String requestPath = path.pathWithinApplication().value();
                    log.error("请求结果: 失败, 请求路径: {}, 耗时: {} ms", requestPath, Duration.between(startTime.get(), LocalDateTime.now()).toMillis(), throwable);
                })
                .doOnSuccess(e -> {
                    String requestPath = path.pathWithinApplication().value();
                    log.info("请求结果: 成功, 请求路径: {}, 耗时: {} ms", requestPath, Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
                });
    }
}
