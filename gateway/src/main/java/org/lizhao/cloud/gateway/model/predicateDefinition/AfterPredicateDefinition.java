package org.lizhao.cloud.gateway.model.predicateDefinition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import org.lizhao.base.exception.CustomException;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;

/**
 * Description AfterPredicateDefinition
 * 时间后抉择器：在规定时间之后接收到的请求可通过
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
public class AfterPredicateDefinition extends PredicateDefinition {

    @JsonIgnore
    private final ZonedDateTime time;

    public AfterPredicateDefinition(@NotNull ZonedDateTime time) {
        super.setName("After");
        super.getArgs().put(NameUtils.generateName(0), time.toString());
        this.time = time;
    }
    public AfterPredicateDefinition(String time) {
        this(ZonedDateTime.parse(time));
    }

    public ZonedDateTime getTime() {
        return ZonedDateTime.parse(super.getArgs().values().stream().findFirst().orElseThrow(CustomException::new));
    }

    public String toYml() {
        return super.getName() + "=" + time.toString();
    }

}
