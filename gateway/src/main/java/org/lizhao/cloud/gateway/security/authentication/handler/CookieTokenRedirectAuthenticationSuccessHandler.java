package org.lizhao.cloud.gateway.security.authentication.handler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Description 登录成功后，将token保存到response cookie
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-26 23:10
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Setter
@Getter
public class CookieTokenRedirectAuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    private String tokenName;
    public CookieTokenRedirectAuthenticationSuccessHandler() {
        super();
    }

    public static CookieTokenRedirectAuthenticationSuccessHandler create(String tokenName, String redirectUrl) {
        CookieTokenRedirectAuthenticationSuccessHandler handler = new CookieTokenRedirectAuthenticationSuccessHandler();
        handler.setLocation(URI.create(redirectUrl));
        handler.setTokenName(tokenName);
        return handler;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("用户验证成功");
        webFilterExchange.getExchange()
                .getResponse()
                .getCookies()
                .set(this.tokenName,
                        ResponseCookie.from(this.tokenName, "").build()
                );
        return super.onAuthenticationSuccess(webFilterExchange, authentication);
    }


}
