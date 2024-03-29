package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description PathPredicateDefinition
 * 请求路径（URL）抉择器：符合指定范围内请求URL的请求可通过
 *                    可同时配置多个请求URL
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class PathPredicateDefinition extends PredicateDefinition {

    private final Set<String> pathMatcherStrSet;
    private final Set<AntPathMatcher> pathMatcherSet;

    public PathPredicateDefinition(@NotNull Set<String> pathMatcherSet) {
        super.setName("Path");
        this.pathMatcherSet = pathMatcherSet.stream().map(e -> new AntPathMatcher(e.toUpperCase())).collect(Collectors.toSet());
        this.pathMatcherStrSet = pathMatcherSet;
        Map<String, String> args = super.getArgs();
        int i = 0;
        for (String pathMatcher : pathMatcherSet) {
            args.put(NameUtils.generateName(i++), pathMatcher);
        }
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", this.pathMatcherStrSet);
    }

}
