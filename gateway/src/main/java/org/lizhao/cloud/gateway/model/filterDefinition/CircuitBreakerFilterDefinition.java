package org.lizhao.cloud.gateway.model.filterDefinition;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.Set;

/**
 * Description CircuitBreakerFilterDefinition
 * 网关定义：断路器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-24 0:42
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class CircuitBreakerFilterDefinition extends FilterDefinition {

    private final String circuitBreakerName;

    private final String fallBackUri;

    private final Set<String> statusCodeSet;

    public CircuitBreakerFilterDefinition(String circuitBreakerName, String fallBackUri, Set<String> statusCodeSet) {
        super();
        super.setName("CircuitBreaker");
        super.getArgs().put(NameUtils.generateName(0), circuitBreakerName);
        super.getArgs().put(NameUtils.generateName(1), fallBackUri);
        if (ObjectUtils.isNotEmpty(statusCodeSet)) {
            int i = 0;
            for (String statusCode : statusCodeSet) {
                super.getArgs().put(NameUtils.generateName(0), statusCode);
            }
        }
        this.circuitBreakerName = circuitBreakerName;
        this.fallBackUri = fallBackUri;
        this.statusCodeSet = statusCodeSet;
    }

}
