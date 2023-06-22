package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

@Getter
public class RemoteAddrPredicateDefinition extends PredicateDefinition {

    private final String remoteAddr;// 192.168.10.20/24

    public RemoteAddrPredicateDefinition(@NotNull String remoteAddr) {
        super.setName("RemoteAddr");
        this.remoteAddr = remoteAddr;
        super.getArgs().put(NameUtils.generateName(0), remoteAddr);
    }

    public String toString() {
        return super.getName() + "=" + this.remoteAddr;
    }

}
