package org.lizhao.cloud.gateway.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Description 自定义用户类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-25 21:49
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class GatewayUser extends User {

    private String id;

    private String phone;

    private String token;

    public GatewayUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public GatewayUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
