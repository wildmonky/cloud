package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;

@Getter
public class QueryPredicateDefinition extends PredicateDefinition {

    private final String paramName;
    private final String paramValue;

    public QueryPredicateDefinition(@NotNull String paramName, String paramValue) {
        super.setName("Query");
        this.paramName = paramName;
        this.paramValue = paramValue;
        super.getArgs().put(NameUtils.generateName(0), paramName);
        super.getArgs().put(NameUtils.generateName(1), paramValue);
    }

    public String toString() {
        return super.getName() + "=" + this.paramName + "," + this.paramValue;
    }

}
