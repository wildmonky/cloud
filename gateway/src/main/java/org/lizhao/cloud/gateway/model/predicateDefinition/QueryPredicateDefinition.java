package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.time.ZonedDateTime;

/**
 * Description QueryPredicateDefinition
 * 请求参数（query parameter）抉择器：
 *                      1、只配置了参数名：含有指定请求参数名的请求可通过
 *                      2、配置了参数名和参数值：含有指定请求参数并且参数值为指定值的请求可通过
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class QueryPredicateDefinition extends PredicateDefinition {

    private final String paramName;
    private final String paramValue;

    public QueryPredicateDefinition(@NotNull String paramName, String paramValue) {
        super.setName("Query");
        this.paramName = paramName;
        this.paramValue = paramValue;
        super.getArgs().put(NameUtils.generateName(0), paramName);
        super.getArgs().put(NameUtils.generateName(1), paramValue);
    }

    public String toYml() {
        return super.getName() + "=" + this.paramName + "," + this.paramValue;
    }

}
