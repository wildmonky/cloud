package org.lizhao.base.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Description 用户
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-19 19:10
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo extends User {

    Set<Role> roles;

    Set<Group> groups;

    /** 数据库中的权限数据 */
    Set<Authority> originAuthorities;

    public SimpleUserInfo transferToSimple() {
        SimpleUserInfo simpleUserInfo = new SimpleUserInfo();
        simpleUserInfo.setId(this.getId());
        simpleUserInfo.setName(this.getName());
        simpleUserInfo.setGroups(Optional.ofNullable(this.groups).orElse(Collections.emptySet()).stream().map(Group::getName).toArray(String[]::new));
        simpleUserInfo.setRoles(Optional.ofNullable(this.roles).orElse(Collections.emptySet()).stream().map(Role::getName).toArray(String[]::new));
        simpleUserInfo.setPermissions(Optional.ofNullable(this.originAuthorities).orElse(Collections.emptySet()).stream().map(authority ->
            new SimpleUserInfo.SimplePermission(authority.getName(), authority.getComment())
        ).toArray(SimpleUserInfo.SimplePermission[]::new));
        return simpleUserInfo;
    }

}
