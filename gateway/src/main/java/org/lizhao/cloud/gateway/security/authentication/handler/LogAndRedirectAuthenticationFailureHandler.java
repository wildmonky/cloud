package org.lizhao.cloud.gateway.security.authentication.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.cloud.gateway.security.XMLHttpRequestRedirectStrategy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

/**
 * Description 认证失败处理器-打印日志并重定向
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-26 23:19
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class LogAndRedirectAuthenticationFailureHandler extends RedirectServerAuthenticationFailureHandler {
    /**
     * Creates an instance
     *
     * @param location the location to redirect to (i.e. "/login?failed")
     */
    public LogAndRedirectAuthenticationFailureHandler(String location) {
        super(location);
        super.setRedirectStrategy(new XMLHttpRequestRedirectStrategy());
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.error("用户验证失败", exception);
        MultiValueMap<String, String> queryParams = webFilterExchange.getExchange().getRequest().getQueryParams();
        String redirectPath = queryParams.getFirst("redirect_path");
        if (StringUtils.isNotBlank(redirectPath)) {
            return Mono.error(new RuntimeException("用户验证失败"));
        }
        return super.onAuthenticationFailure(webFilterExchange, exception);
    }

}
