package org.lizhao.cloud.gateway.filter.httpHeader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lizhao.base.constant.SecurityConstant;
import org.lizhao.base.model.UserInfo;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description 添加用户信息
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-21 14:18
 * @since 0.0.1-SNAPSHOT
 */
public class AddUserInfoRequestHeaderGlobalFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(AddUserInfoRequestHeaderGlobalFilter.class);
    /**
     * 在 {@link ReactiveLoadBalancerClientFilter#LOAD_BALANCER_CLIENT_FILTER_ORDER} 前
     */
    private static final int ADD_USER_INFO_REQUEST_HEADER_FILTER_ORDER = ReactiveLoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER - 1;
    private final ObjectMapper objectMapper;

    public AddUserInfoRequestHeaderGlobalFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    UserInfo userInfo = (UserInfo)authentication.getPrincipal();
                    ServerHttpRequest request = exchange.getRequest();
                    request.mutate()
                            .headers(httpHeaders -> {
                                String userInfoStr;
                                try {
                                    userInfoStr = objectMapper.writeValueAsString(userInfo);
                                } catch (JsonProcessingException e) {
                                    log.error(request.getPath().value() + " ,用户信息请求头添加失败", e);
                                    throw new RuntimeException(e);
                                }
                                httpHeaders.add(SecurityConstant.USER_IN_HEADER, userInfoStr);
                            }).build();
                    return chain.filter(exchange.mutate().request(request).build());
                });
    }

    @Override
    public int getOrder() {
        return ADD_USER_INFO_REQUEST_HEADER_FILTER_ORDER;
    }
}
