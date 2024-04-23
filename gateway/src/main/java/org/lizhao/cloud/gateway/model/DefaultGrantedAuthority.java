package org.lizhao.cloud.gateway.model;

import org.lizhao.base.entity.authority.Authority;
import org.springframework.security.core.GrantedAuthority;

/**
 * Description 默认 授权实现类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-19 20:02
 * @since 0.0.1-SNAPSHOT
 */
public class DefaultGrantedAuthority implements GrantedAuthority {

    private final Authority authority;

    public DefaultGrantedAuthority(Authority authority) {
        this.authority = authority;
    }

    /**
     * 获取权限详情：id 等信息
     * @return 数据库中对应的权限信息
     */
    public Authority getDetails() {
        return authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.getName();
    }

}
