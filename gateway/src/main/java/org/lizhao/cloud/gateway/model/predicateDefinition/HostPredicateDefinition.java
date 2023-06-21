package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;

/**
 * Description HostPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class HostPredicateDefinition {

    private final String name = "Host";
    private final Set<String> hostPatternSet;

    public HostPredicateDefinition(@NotNull Set<String> hostPatternSet) {
        this.hostPatternSet = hostPatternSet;
    }

    public String toString() {
        return this.name + "=" + String.join(",", hostPatternSet);
    }

}
