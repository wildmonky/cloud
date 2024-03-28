package org.lizhao.cloud.gateway.security.security.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-28 22:09
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class RedisSecurityContextImpl extends SecurityContextImpl {

    private String token;

    public RedisSecurityContextImpl(Authentication authentication, String token) {
        super(authentication);
        this.token = token;
    }

}
