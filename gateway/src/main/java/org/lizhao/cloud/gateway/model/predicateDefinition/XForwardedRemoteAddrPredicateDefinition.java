package org.lizhao.cloud.gateway.model.predicateDefinition;

import com.alibaba.nacos.shaded.com.google.common.collect.Sets;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.Set;

/**
 * Description XForwardedRemoteAddrPredicateDefinition
 * X-Forwarded-For(XFF): <client>, <proxy1>, <proxy2> 该请求头内容可以被伪造
 * X-Forwarded-For(XFF)远端主机地址抉择器：使用X-Forwarded-For(XFF)请求头解析获得的remote addr在指定网段内的请求可通过
 * IP addr/subnet mask (<a href="https://whatismyipaddress.com/cidr">CIDR-notation</a>)
 * 192.168.16.10/24 193.168.16网段下的主机
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Slf4j
public class XForwardedRemoteAddrPredicateDefinition extends PredicateDefinition {

    private final Set<String> xForwardedRemoteAddrSet;

    public XForwardedRemoteAddrPredicateDefinition(@NotNull String... xForwardedRemoteAddrSet) {
        super.setName("XForwardedRemoteAddr");
        this.xForwardedRemoteAddrSet = Sets.newHashSet(xForwardedRemoteAddrSet);
        int i = 0;
        for (String xForwardedRemoteAddr : xForwardedRemoteAddrSet) {
            super.getArgs().put(NameUtils.generateName(i++), xForwardedRemoteAddr);
        }
    }

    public String toYml() {
        return super.getName() + "=" + String.join(",", this.xForwardedRemoteAddrSet);
    }

}
