package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Description BetweenPredicateDefinition
 * 时间区域抉择器：在规定时间区域内接收到的请求可通过
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class BetweenPredicateDefinition extends PredicateDefinition {

    private final ZonedDateTime startTime;
    private final ZonedDateTime endTime;

    public BetweenPredicateDefinition(@NotNull ZonedDateTime startTime, @NotNull ZonedDateTime endTime) {
        super.setName("Between");
        this.startTime = startTime;
        this.endTime = endTime;
        Map<String, String> args = super.getArgs();
        args.put(NameUtils.generateName(0), startTime.toString());
        args.put(NameUtils.generateName(1), endTime.toString());
    }
    public BetweenPredicateDefinition(String startTime, String endTime) {
        this(ZonedDateTime.parse(startTime), ZonedDateTime.parse(endTime));
    }

    public String toYml() {
        return super.getName() +
                "=" +
                this.startTime.toString() +
                ", " +
                this.endTime.toString();
    }

}
