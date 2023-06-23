package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

/**
 * Description: WeightPredicateDefinition
 * 比重抉择器：负载均衡
 *
 * - id: weight_high
 *       uri: https://weighthigh.org
 *       predicates:
 *        - Weight=group1, 8
 * - id: weight_low
 *       uri: https://weightlow.org
 *       predicates:
 *        - Weight=group1, 2
 * This route would forward ~80% of traffic to weighthigh.org and ~20% of traffic to weighlow.org
 */
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

    public String toYml() {
        return super.getName() + "=" + this.group + "," + this.weight;
    }

}
