package org.lizhao.cloud.gateway.security.userdetailsservice;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.enums.UserStatusEnum;
import org.lizhao.cloud.gateway.entity.authority.Authority;
import org.lizhao.cloud.gateway.entity.authority.Role;
import org.lizhao.cloud.gateway.entity.relation.*;
import org.lizhao.cloud.gateway.entity.user.Group;
import org.lizhao.cloud.gateway.entity.user.User;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.model.UserHolder;
import org.lizhao.cloud.gateway.repository.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Description 用户信息存储在reactive 数据库中
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:15
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class DBReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final SecurityProperties securityProperties;
    /**
     * spring security 在进行校验时，
     * 是将密码原文与加密后的密码进行比较，
     * 在返回UserDetails时，设置密码为加密后的
     * @see PasswordEncoder#matches(CharSequence, String)
     */
    private final ObjectProvider<PasswordEncoder> passwordEncoderObjectProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final AuthorityRepository authorityRepository;
    private final GroupUserRelationRepository groupUserRelationRepository;
    private final GroupAuthorityRelationRepository groupAuthorityRelationRepository;
    private final UserRoleRelationRepository userRoleRelationRepository;
    private final UserAuthorityRelationRepository userAuthorityRelationRepository;
    private final RoleAuthorityRelationRepository roleAuthorityRelationRepository;



    public DBReactiveUserDetailsService(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder,
                                        UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        GroupRepository groupRepository,
                                        AuthorityRepository authorityRepository,
                                        GroupUserRelationRepository groupUserRelationRepository,
                                        GroupAuthorityRelationRepository groupAuthorityRelationRepository,
                                        UserRoleRelationRepository userRoleRelationRepository,
                                        UserAuthorityRelationRepository userAuthorityRelationRepository,
                                        RoleAuthorityRelationRepository roleAuthorityRelationRepository

    ) {
        this.securityProperties = properties;
        this.passwordEncoderObjectProvider = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.authorityRepository = authorityRepository;
        this.groupUserRelationRepository = groupUserRelationRepository;
        this.groupAuthorityRelationRepository = groupAuthorityRelationRepository;
        this.userRoleRelationRepository = userRoleRelationRepository;
        this.userAuthorityRelationRepository = userAuthorityRelationRepository;
        this.roleAuthorityRelationRepository = roleAuthorityRelationRepository;
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        assert user instanceof  GatewayUser;
        GatewayUser gatewayUser = (GatewayUser) user;
        User dbUser = User.builder()
                .id(gatewayUser.getId())
                .name(gatewayUser.getUsername())
                .phone(gatewayUser.getPhone())
                .password(newPassword)
                .status(1)
                .build();
        UserDetails currentUser = UserHolder.getCurrentUser();
        assert currentUser instanceof GatewayUser;
        GatewayUser loginUser = (GatewayUser) currentUser;
        dbUser.setUpdateUseId(loginUser.getId());
        dbUser.setUpdateUseName(loginUser.getUsername());
        dbUser.setUpdateTime(LocalDateTime.now());
        return userRepository.save(dbUser)
                .map(in -> org.springframework.security.core.userdetails.User
                .withUserDetails(user)
                .password(in.getPassword())
                .build()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        User user = new User();
        user.setName(username);
        return userRepository.findOne(Example.of(user))
                .flatMap(e ->
                        // 获取用户绑定权限
                        userAuthorities(e.getId())
                                // 获取用户拥有的角色所绑定的权限
                                .concatWith(roleAuthorities(userRoles(e.getId()).map(Role::getId)))
                                // 获取用户所在组所绑定的权限
                                .concatWith(groupAuthorities(userGroups(e.getId()).map(Group::getId)))
                                .collectList()
                                .map(authorities -> translate(e, Collections.emptyList()))
                );
    }

    /**
     * 查询 用户绑定的用户组
     *
     * @param userId 用户id
     * @return
     */
    private Flux<Group> userGroups(String userId) {
        GroupUserRelation relation = new GroupUserRelation();
        relation.setUserId(userId);
        return groupUserRelationRepository.findAll(Example.of(relation))
                .flatMap(e -> groupRepository.findById(e.getGroupId()));
    }

    /**
     * 查询用户绑定的角色
     * @param userId
     * @return
     */
    private Flux<Role> userRoles(String userId) {
        UserRoleRelation relation = new UserRoleRelation();
        relation.setUserId(userId);
        return userRoleRelationRepository.findAll(Example.of(relation))
                .flatMap(e -> roleRepository.findById(e.getRoleId()));
    }

    /**
     * 查询用户绑定的权限
     * @param userId
     * @return
     */
    private Flux<Authority> userAuthorities(String userId) {
        UserAuthorityRelation relation = new UserAuthorityRelation();
        relation.setUserId(userId);
        return userAuthorityRelationRepository.findAll(Example.of(relation))
                .flatMap(e -> authorityRepository.findById(e.getAuthorityId()));
    }

    /**
     * 查询角色绑定权限
     * @param roleIdFlux
     * @return
     */
    private Flux<Authority> roleAuthorities(Flux<String> roleIdFlux) {
        return roleIdFlux.flatMap(roleId -> {
            RoleAuthorityRelation relation = new RoleAuthorityRelation();
            relation.setRoleId(roleId);
            return roleAuthorityRelationRepository.findAll(Example.of(relation));
        }).flatMap(rel -> authorityRepository.findById(rel.getAuthorityId()));
    }

    /**
     * 查询组绑定的权限
     * @param groupIdFlux
     * @return
     */
    private Flux<Authority> groupAuthorities(Flux<String> groupIdFlux) {
        return groupIdFlux.flatMap(groupId -> {
            GroupAuthorityRelation relation = new GroupAuthorityRelation();
            relation.setGroupId(groupId);
            return groupAuthorityRelationRepository.findAll(Example.of(relation));
        }).flatMap(rel -> authorityRepository.findById(rel.getAuthorityId()));
    }


    private GatewayUser translate(User user, Collection<Authority> authorities) {
        assert user != null;
        GatewayUser gatewayUser = new GatewayUser(user.getName(),
                Objects.requireNonNull(passwordEncoderObjectProvider.getIfAvailable()).encode(user.getPassword()),
                Objects.equals(UserStatusEnum.NORMAL.getCode(), user.getStatus()),
                true,
                true,
                !Objects.equals(UserStatusEnum.LOCK.getCode(), user.getStatus()),
                authorities
        );
        gatewayUser.setId(user.getId());
        gatewayUser.setPhone(user.getPhone());
        return gatewayUser;
    }

}
