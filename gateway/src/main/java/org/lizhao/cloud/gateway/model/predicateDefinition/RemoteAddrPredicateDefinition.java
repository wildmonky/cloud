package org.lizhao.cloud.gateway.model.predicateDefinition;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;

/**
 * Description RemoteAddrPredicateDefinition
 * 远端主机地址（请求客户端地址）抉择器：请求客户端主机地址在指定的网段内的请求可通过
 * IP addr/subnet mask (<a href="https://whatismyipaddress.com/cidr">CIDR-notation</a>)
 * 192.168.16.10/24 193.168.16网段下的主机
 *
 * 😎注意：现在很多请求都是通过代理服务器转发的。
 * 因此使用RemoteAddrPredicateDefinition时，并不一定获得期望的结果。
 * 客户端可以通过代理服务器绕过这一限制。
 * 建议使用 {@link XForwardedRemoteAddrPredicateDefinition} maxTrustedIndex=1 默认使用X-Forwarded-For中的最后代理ip
 * 或者自定义远端地址解析{@link RemoteAddressResolver}，
 * 或者直接使用其实现类{@link XForwardedRemoteAddressResolver} 可配置maxTrustedIndex，选择可行的ip
 * ,通过<a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/X-Forwarded-For">X-Forwarded-For</a>请求头获取请求客户端地址
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-21 23:16
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class RemoteAddrPredicateDefinition extends PredicateDefinition {

    // 192.168.10.20/24
    private final String remoteAddr;

    public RemoteAddrPredicateDefinition(@NotNull String remoteAddr) {
        super.setName("RemoteAddr");
        this.remoteAddr = remoteAddr;
        super.getArgs().put(NameUtils.generateName(0), remoteAddr);
    }

    public String toYml() {
        return super.getName() + "=" + this.remoteAddr;
    }

}
