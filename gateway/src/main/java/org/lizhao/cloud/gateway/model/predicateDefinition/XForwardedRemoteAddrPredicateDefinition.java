package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.Set;

/**
 * This route matches if the X-Forwarded-For header contains, for example, 192.168.1.10
 */
@Getter
@Slf4j
public class XForwardedRemoteAddrPredicateDefinition extends PredicateDefinition {

    private final Set<String> xForwardedRemoteAddrSet;

    public XForwardedRemoteAddrPredicateDefinition(@NotNull Set<String> xForwardedRemoteAddrSet) {
        super.setName("XForwardedRemoteAddr");
        this.xForwardedRemoteAddrSet = xForwardedRemoteAddrSet;
        int i = 0;
        for (String xForwardedRemoteAddr : xForwardedRemoteAddrSet) {
            super.getArgs().put(NameUtils.generateName(i++), xForwardedRemoteAddr);
        }
    }

    public String toString() {
        return super.getName() + "=" + String.join(",", this.xForwardedRemoteAddrSet);
    }

}
