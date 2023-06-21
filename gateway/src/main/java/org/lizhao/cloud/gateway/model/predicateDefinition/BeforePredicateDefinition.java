package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.time.ZonedDateTime;

/**
 * Description BeforePredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class BeforePredicateDefinition {

    private final String name = "Before";
    private final ZonedDateTime time;

    public BeforePredicateDefinition(@NotNull ZonedDateTime time) {
        this.time = time;
    }
    public BeforePredicateDefinition(String time) {
        this.time = ZonedDateTime.parse(time);
    }

    public String toString() {
        return this.name + "=" + time.toString();
    }

}
