package org.lizhao.user.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.constant.UserConstant;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.model.UserInfoHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
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
 *             计算请求执行时间;
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-11 20:27
 * @since 0.0.1-SNAPSHOT
 * @see org.springframework.security.config.web.server.SecurityWebFiltersOrder
 * @see Ordered
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE) // SecurityWebFiltersOrder.FIRST
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class RequestIntercept implements WebFilter {

    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Description
     * 解析 用户信息
     *
     * @since 0.0.1-SNAPSHOT
     * @author lizhao
     * @date 2024/4/2 21:27
     * @param exchange exchange
     * @param chain chain
     * @return reactor.core.publisher.Mono<java.lang.Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        AtomicReference<LocalDateTime> startTime = new AtomicReference<>(LocalDateTime.now());

        ServerHttpRequest request = exchange.getRequest();
        // 解析 请求头获取用户信息（网关封装）
        HttpHeaders headers = request.getHeaders();
        String userInfoStr = headers.getFirst(UserConstant.USER_IN_HEADER);
        if (StringUtils.isNotBlank(userInfoStr)) {
            try {
                UserInfo userInfo = objectMapper.readValue(userInfoStr, UserInfo.class);
                UserInfoHolder.set(userInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

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
                }).doFinally(e -> {
                    UserInfoHolder.remove();
                });
    }
}
