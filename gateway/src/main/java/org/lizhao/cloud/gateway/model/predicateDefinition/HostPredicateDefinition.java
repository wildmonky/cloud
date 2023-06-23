package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.Map;
import java.util.Set;

/**
 * Description HostPredicateDefinition
 * 请求客户端域名抉择器：来自指定的主机域名范围内的请求可通过
 *                      可同时指定多个主机域名，支持正则表达式
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class HostPredicateDefinition extends PredicateDefinition {

    private final Set<String> hostPatternSet;

    public HostPredicateDefinition(@NotNull Set<String> hostPatternSet) {
        super.setName("Host");
        this.hostPatternSet = hostPatternSet;
        Map<String, String> args = super.getArgs();
        int i = 0;
        for (String host : hostPatternSet) {
            args.put(NameUtils.generateName(i++), host);
        }
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", hostPatternSet);
    }

}
