package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.ZonedDateTime;

/**
 * Description AfterPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class AfterPredicateDefinition {

    private final String name = "After";
    private final ZonedDateTime time;

    public AfterPredicateDefinition(@NotNull ZonedDateTime time) {
        this.time = time;
    }
    public AfterPredicateDefinition(String time) {
        this.time = ZonedDateTime.parse(time);
    }

    public String toString() {
        return this.name + "=" + time.toString();
    }

}
