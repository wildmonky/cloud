package org.lizhao.cloud.gateway.filter.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description 从头部开始删除路径：
 * stripNum: 1
 * /users/login ---> /login
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-27 20:56
 * @since 0.0.1-SNAPSHOT
 */
public class StripPrefixGatewayFilter implements GatewayFilter, Ordered {

    public static final int STRIP_PREFIX_FILTER_ORDER = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER - 1;

    private final int stripNum;

    public StripPrefixGatewayFilter(int stripNum) {
        this.stripNum = stripNum;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerWebExchangeUtils.addOriginalRequestUrl(exchange, request.getURI());
        String path = request.getURI().getRawPath();
        String[] originalParts = org.springframework.util.StringUtils.tokenizeToStringArray(path, "/");
        StringBuilder newPath = new StringBuilder("/");

        for(int i = 0; i < originalParts.length; ++i) {
            if (i >= stripNum) {
                if (newPath.length() > 1) {
                    newPath.append('/');
                }

                newPath.append(originalParts[i]);
            }
        }

        if (newPath.length() > 1 && path.endsWith("/")) {
            newPath.append('/');
        }

        ServerHttpRequest newRequest = request.mutate().path(newPath.toString()).build();
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    @Override
    public int getOrder() {
        return STRIP_PREFIX_FILTER_ORDER;
    }
}
