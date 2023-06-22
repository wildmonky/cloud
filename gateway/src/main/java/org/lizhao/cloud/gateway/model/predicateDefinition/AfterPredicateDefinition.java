package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;

@Getter
public class AfterPredicateDefinition extends PredicateDefinition {

    private final ZonedDateTime time;

    public AfterPredicateDefinition(@NotNull ZonedDateTime time) {
        super.setName("After");
        super.getArgs().put(NameUtils.generateName(0), time.toString());
        this.time = time;
    }
    public AfterPredicateDefinition(String time) {
        this(ZonedDateTime.parse(time));
    }

    public String toString() {
        return super.getName() + "=" + time.toString();
    }

}
