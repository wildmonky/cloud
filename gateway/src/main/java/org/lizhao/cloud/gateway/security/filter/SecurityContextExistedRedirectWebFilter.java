package org.lizhao.cloud.gateway.security.filter;

import org.lizhao.cloud.gateway.configurer.properties.SecurityProperties;
import org.lizhao.cloud.gateway.security.XMLHttpRequestRedirectStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.context.ReactorContextWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Description 避免重复登录
 * 在 {@link ReactorContextWebFilter} 后，
 * 当使用 登录路径 访问时，检测是否加载到SecurityContext：
 *      1、加载到：已经登录，跳转首页
 *      2、未加载到：继续
 * SecurityWebFiltersOrder#REACTOR_CONTEXT.getOrder() + 1
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-01 11:30
 * @since 0.0.1-SNAPSHOT
 * @see SecurityWebFiltersOrder#REACTOR_CONTEXT
 */
@Order(501)
@Component
public class SecurityContextExistedRedirectWebFilter implements WebFilter {

    private final SecurityProperties securityProperties;

    private final ServerWebExchangeMatcher requiresHttpsRedirectMatcher;

    private final ServerRedirectStrategy redirectStrategy = new XMLHttpRequestRedirectStrategy();

    public SecurityContextExistedRedirectWebFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.requiresHttpsRedirectMatcher = ServerWebExchangeMatchers
                .pathMatchers(securityProperties.getUrl().getLoginPath());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.just(new SecurityContextImpl()))
                .flatMap(securityContext ->
                    requiresHttpsRedirectMatcher.matches(exchange)
                            .flatMap(matchResult -> {
                                if(securityContext.getAuthentication() != null && matchResult.isMatch()) {
                                    return ServerWebExchangeMatcher.MatchResult.match();
                                }
                                return ServerWebExchangeMatcher.MatchResult.notMatch();
                            })
                ).filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(s ->
                    redirectStrategy.sendRedirect(exchange, URI.create(securityProperties.getUrl().getIndexPath())).then(Mono.empty())
                );
    }
}
