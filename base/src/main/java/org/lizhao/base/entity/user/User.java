package org.lizhao.base.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.authority.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Description 用户实体类
 *
 * 系统用户实体信息：保存登录名和密码
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 15:55
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends CommonAttribute implements UserDetails {

    /**
     * 用户账号主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
    private String id;

    /**
     * 用户识别码，可以直接使用Id识别
     */
    @Column
    private String identity;

    /**
     * 用户登录账号
     */
    @Column
    private String username;

    /**
     * 用户登录密码
     */
    @Column
    private String password;

    @OneToMany
    private Set<Authority> authorities;

    /**
     * 用户状态: 0-初始; 1-可用; 2-禁用
     */
    @Column
    private Integer status;

    public static User of(String id, String identity, String name, String password, Set<Authority> authorities, int status) {
        return new User(id, identity, name, password, authorities, status);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public void setGrantedAuthoritySet(Set<Authority> grantedAuthoritySet) {
        this.authorities = grantedAuthoritySet;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
