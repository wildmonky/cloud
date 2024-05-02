package org.lizhao.cloud.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.enums.CommonStateEnum;
import org.lizhao.base.model.LoginUserInfo;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.utils.BaseUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author lizhao
 */
public class GatewayUser extends UserInfo implements UserDetails {

    @Getter
    @Setter
    private String token;

    private final Set<? extends GrantedAuthority> grantedAuthorities;

    @JsonCreator
    public GatewayUser(@JsonProperty("groups") Set<Group> groups,
                       @JsonProperty("roles") Set<Role> roles,
                       @JsonProperty("authorities") Set<Authority> authorities) {
        super();
        super.setGroups(groups);
        super.setRoles(roles);
        this.grantedAuthorities = Optional.ofNullable(authorities).orElse(Collections.emptySet())
                .stream().map(DefaultGrantedAuthority::new).collect(Collectors.toSet());
    }

    public GatewayUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.setUserInfo(user);
        this.grantedAuthorities = new HashSet<>(authorities);
    }

    public GatewayUser(UserInfo userInfo) {
        this(userInfo.getGroups(), userInfo.getRoles(), userInfo.getOriginAuthorities());
        this.setUserInfo(userInfo);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !CommonStateEnum.LOCKED.check(super.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 构建函数中使用时，必须在super()后
     * @param user 用户信息
     */
    private void setUserInfo(User user) {
        super.setId(user.getId());
        super.setName(user.getName());
        super.setPhone(user.getPhone());
        super.setPassword(user.getPassword());
        super.setStatus(user.getStatus());
        super.setCreateUseId(user.getCreateUseId());
        super.setCreateUseName(user.getCreateUseName());
        super.setCreateTime(user.getCreateTime());
        super.setUpdateUseId(user.getUpdateUseId());
        super.setUpdateUseName(user.getUpdateUseName());
        super.setUpdateTime(user.getUpdateTime());
    }

    public LoginUserInfo transferToLogin() {
        SimpleUserInfo simpleUserInfo = transferToSimple();
        LoginUserInfo loginUserInfo = BaseUtils.copy(simpleUserInfo, LoginUserInfo.class);
        loginUserInfo.setToken(this.token);
        return loginUserInfo;
    }

    public boolean isAnonymous() {
        return this.getUsername().equals("anonymous");
    }

    public static class GatewayUserBuilder {

        public static GatewayUser anonymous() {
            GatewayUser anonymous = new GatewayUser(null, null, null);
            anonymous.setName("anonymous");
            anonymous.setStatus(CommonStateEnum.NORMAL.getCode());
            return anonymous;
        }

    }

}
