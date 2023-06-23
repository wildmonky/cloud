package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;

/**
 * Description BeforePredicateDefinition
 * 时间前抉择器：在规定时间之前接收到的的请求可通过
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
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

    public String toYml() {
        return super.getName() + "=" + time.toString();
    }

}
