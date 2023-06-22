package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description BetweenPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class BetweenPredicateDefinition extends PredicateDefinition {

    private final List<ZonedDateTime> timeList = new ArrayList<>(2);

    public BetweenPredicateDefinition(@NotNull ZonedDateTime startTime, @NotNull ZonedDateTime endTime) {
        super.setName("Between");
        this.timeList.add(startTime);
        this.timeList.add(endTime);
        Map<String, String> args = super.getArgs();
        args.put(NameUtils.generateName(0), startTime.toString());
        args.put(NameUtils.generateName(1), endTime.toString());
    }
    public BetweenPredicateDefinition(String startTime, String endTime) {
        this(ZonedDateTime.parse(startTime), ZonedDateTime.parse(endTime));
    }

    public String toString() {
        return super.getName() +
                "=" +
                this.timeList.get(0).toString() +
                ", " +
                this.timeList.get(1).toString();
    }

}
