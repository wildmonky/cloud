package org.lizhao.cloud.gateway.security.csrf;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author lizhao
 */
public class CsrfRequestMatcher implements ServerWebExchangeMatcher {

    private final String[] excludePaths;

    public CsrfRequestMatcher(String... excludePaths) {
        this.excludePaths = excludePaths;
    }

    public static CsrfRequestMatcher.Builder builder() {
        return new CsrfRequestMatcher.Builder();
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().pathWithinApplication().toString();
        AntPathMatcher matcher = new AntPathMatcher();
        return Arrays.stream(excludePaths)
                .anyMatch(p -> matcher.match(p, path)) ? MatchResult.notMatch() : MatchResult.match();
    }

    public static class Builder{

        private final List<String> excludePathList = new ArrayList<>();

        public Builder() {}

        public Builder exclude(String... moreExcludePath) {
            excludePathList.addAll(Arrays.asList(moreExcludePath));
            return this;
        }

        public CsrfRequestMatcher build() {
            return new CsrfRequestMatcher(this.excludePathList.toArray(new String[0]));
        }

    }

}
