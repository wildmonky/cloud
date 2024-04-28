package org.lizhao.base.web.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.constant.SecurityConstant;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.ReactiveUserInfoHolder;
import org.lizhao.base.model.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

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
public class ReactiveRequestIntercept implements WebFilter {

    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    private final ObjectMapper objectMapper;

    public ReactiveRequestIntercept(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
        String userInfoStr = headers.getFirst(SecurityConstant.USER_IN_HEADER);
        UserInfo userInfo = null;
        if (StringUtils.isNotBlank(userInfoStr)) {
            try {
                userInfo = objectMapper.readValue(userInfoStr, UserInfo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        RequestPath path = request.getPath();
        HttpMethod httpMethod = request.getMethod();

        Mono<Void> result;
//        if (StringUtils.isNotBlank(contextPath) & path.value().startsWith(contextPath)) {
//            result = chain.filter(
//                    exchange.mutate()
//                            .request(request.mutate().contextPath(contextPath).build())
//                            .build());
//        } else {
            result = chain.filter(exchange);
//        }
        final UserInfo finalUserInfo = userInfo;
        return result
//                .doFirst(() -> UserHolder.setCurrentUser(new GatewayUser("1", "1", Collections.emptySet()))) // 内置测试用户
//                .doOnRequest(e -> startTime.set(LocalDateTime.now()))
                .doOnError(throwable -> {
                    String requestPath = path.pathWithinApplication().value();
                    if (throwable instanceof MessageException) {
                        log.info("请求方法: {}, 请求路径: {}, 请求结果: {}, 耗时: {} ms", httpMethod, requestPath, throwable.getMessage(), Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
                        return;
                    }
                    log.error("请求方法: {}, 请求路径: {}, 请求结果: 失败, 耗时: {} ms", httpMethod, requestPath, Duration.between(startTime.get(), LocalDateTime.now()).toMillis(), throwable);
                })
                .doOnSuccess(e -> {
                    String requestPath = path.pathWithinApplication().value();
                    log.info("请求方法: {}, 请求路径: {}, 请求结果: 成功, 耗时: {} ms", httpMethod, requestPath, Duration.between(startTime.get(), LocalDateTime.now()).toMillis());
                })
                .contextWrite(mainContext -> {
                    //用户信息保存至上下文
                    if (finalUserInfo != null) {
                        ContextView contextView = ReactiveUserInfoHolder.withUserInfo(finalUserInfo);
                        return mainContext.putAll(contextView);
                    }
                    return mainContext;
                });
    }
}
