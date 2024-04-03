package org.lizhao.cloud.gateway.security.authentication.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-30 21:38
 * @since 0.0.1-SNAPSHOT
 */
public class ServerJwtAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        HttpCookie token = exchange.getRequest().getCookies().getFirst("TOKEN");
        if (token == null) {
            return Mono.empty();
        }
        String tokenValue = token.getValue();
        assert StringUtils.isNotBlank(tokenValue);
        return Mono.just(UsernamePasswordAuthenticationToken.unauthenticated(tokenValue, null));
    }

}
