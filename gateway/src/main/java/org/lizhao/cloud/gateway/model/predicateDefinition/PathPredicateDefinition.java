package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description PathPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class PathPredicateDefinition {

    private final String name = "Path";
    private final Set<String> pathMatcherStrSet;
    private final Set<AntPathMatcher> pathMatcherSet;

    public PathPredicateDefinition(@NotNull Set<String> pathMatcherSet) {
        this.pathMatcherSet = pathMatcherSet.stream().map(e -> new AntPathMatcher(e.toUpperCase())).collect(Collectors.toSet());
        this.pathMatcherStrSet = pathMatcherSet;
    }

    public String toString() {
        return this.name + "=" + String.join(",", this.pathMatcherStrSet);
    }

}
