package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

@Getter
@Slf4j
public class WeightPredicateDefinition extends PredicateDefinition {

    private final String group;
    private final String weight;

    public WeightPredicateDefinition(@NotNull String group, String weightStr) {
        try{
            int weight = Integer.parseInt(weightStr);
        }catch (NumberFormatException e) {
            log.error("weight必须是数字：{}", weightStr);
            e.printStackTrace();
        }
        super.setName("Weight");
        this.group = group;
        this.weight = weightStr;
        super.getArgs().put(NameUtils.generateName(0), group);
        super.getArgs().put(NameUtils.generateName(1), weightStr);
    }

    public String toString() {
        return super.getName() + "=" + this.group + "," + this.weight;
    }

}
