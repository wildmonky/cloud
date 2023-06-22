package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;

@Getter
public class BeforePredicateDefinition extends PredicateDefinition {

    private final ZonedDateTime time;

    public BeforePredicateDefinition(@NotNull ZonedDateTime time) {
        super.setName("Before");
        super.getArgs().put(NameUtils.generateName(0), time.toString());
        this.time = time;
    }
    public BeforePredicateDefinition(String time) {
        this(ZonedDateTime.parse(time));
    }

    public String toString() {
        return super.getName() + "=" + time.toString();
    }

}
