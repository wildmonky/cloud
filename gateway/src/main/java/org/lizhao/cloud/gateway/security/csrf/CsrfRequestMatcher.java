package org.lizhao.cloud.gateway.security.csrf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class CsrfRequestMatcher implements ServerWebExchangeMatcher {

    private final String[] excludePaths;

    public CsrfRequestMatcher(String... excludePaths) {
        this.excludePaths = excludePaths;
    }

    public static CsrfRequestMatcher exclude(String... excludePaths) {
        return new CsrfRequestMatcher(excludePaths);
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().pathWithinApplication().toString();
        return Arrays.stream(excludePaths).anyMatch(p -> StringUtils.equalsIgnoreCase(path, p)) ? MatchResult.notMatch() : MatchResult.match();
    }

}
