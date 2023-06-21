package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description HostPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class MethodPredicateDefinition {

    private final String name = "Method";
    private final Set<HttpMethod> httpMethodSet;

    public MethodPredicateDefinition(@NotNull Set<String> httpMethodSet) {
        this.httpMethodSet = httpMethodSet.stream().map(e -> HttpMethod.valueOf(e.toUpperCase())).collect(Collectors.toSet());
    }

    public String toString() {
        Set<String> methodNameSet = this.httpMethodSet.stream().map(HttpMethod::name).collect(Collectors.toSet());
        return this.name + "=" + String.join(",", methodNameSet);
    }

}
