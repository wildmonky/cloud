package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description HostPredicateDefinition
 * 请求方法抉择器：使用指定范围内的请求方法的请求可通过
 *              可同时配置多个请求方法
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class MethodPredicateDefinition extends PredicateDefinition {

    private final Set<HttpMethod> httpMethodSet;

    public MethodPredicateDefinition(@NotNull Set<String> httpMethodSet) {
        super.setName("Method");
        this.httpMethodSet = httpMethodSet.stream().map(e -> HttpMethod.valueOf(e.toUpperCase())).collect(Collectors.toSet());
        Map<String, String> args = super.getArgs();
        int i = 0;
        for (String method : httpMethodSet) {
            args.put(NameUtils.generateName(i++), method);
        }
    }

    public String toYml() {
        Set<String> methodNameSet = this.httpMethodSet.stream().map(HttpMethod::name).collect(Collectors.toSet());
        return super.getName() + "=" + String.join(",", methodNameSet);
    }

}
