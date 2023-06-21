package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description BetweenPredicateDefinition
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class BetweenPredicateDefinition {

    private final String name = "Between";
    private final List<ZonedDateTime> timeList = new ArrayList<>(2);

    public BetweenPredicateDefinition(@NotNull ZonedDateTime startTime, @NotNull ZonedDateTime endTime) {
        this.timeList.add(startTime);
        this.timeList.add(endTime);
    }
    public BetweenPredicateDefinition(String startTime, String endTime) {
        this.timeList.add(ZonedDateTime.parse(startTime));
        this.timeList.add(ZonedDateTime.parse(endTime));
    }

    public String toString() {
        return this.name +
                "=" +
                this.timeList.get(0).toString() +
                ", " +
                this.timeList.get(1).toString();
    }

}
