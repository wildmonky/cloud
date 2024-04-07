package org.lizhao.cloud.gateway.security.userdetailsservice;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.enums.UserStatusEnum;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.handler.UserHandler;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.repository.*;
import org.lizhao.cloud.gateway.security.authentication.TokenAuthenticationToken;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description 用户信息存储在reactive 数据库中
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:15
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class DBReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final SecurityProperties securityProperties;
    /**
     * spring security 在进行校验时，
     * 是将密码原文与加密后的密码进行比较，
     * 在返回UserDetails时，设置密码为加密后的
     * @see PasswordEncoder#matches(CharSequence, String)
     */
    private final ObjectProvider<PasswordEncoder> passwordEncoderObjectProvider;
    private final UserRepository userRepository;

    private final UserHandler userHandler;


    public DBReactiveUserDetailsServiceImpl(SecurityProperties properties,
                                            ObjectProvider<PasswordEncoder> passwordEncoder,
                                            UserRepository userRepository,
                                            UserHandler userHandler
    ) {
        this.securityProperties = properties;
        this.passwordEncoderObjectProvider = passwordEncoder;
        this.userRepository = userRepository;
        this.userHandler = userHandler;
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
        return ReactiveSecurityContextHolder.getContext().flatMap(securityContext -> {
            TokenAuthenticationToken authentication = (TokenAuthenticationToken) securityContext.getAuthentication();
            GatewayUser currentUser = (GatewayUser) authentication.getPrincipal();
            dbUser.setUpdateUseId(currentUser.getId());
            dbUser.setUpdateUseName(currentUser.getUsername());
            dbUser.setUpdateTime(LocalDateTime.now());
            return userRepository.save(dbUser)
                    .map(in -> org.springframework.security.core.userdetails.User
                            .withUserDetails(user)
                            .password(in.getPassword())
                            .build()
                    );
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        User user = new User();
        user.setName(username);
        return userRepository.findOne(Example.of(user))
                .flatMap(e ->
                        // 获取用户绑定权限
                        userHandler.userAllAuthorities(e)
                                .collectList()
                                .map(authorities -> translate(e, Collections.emptyList()))
                );
    }

    /**
     * 权限转换
     *
     * @param user
     * @param authorities
     * @return
     */
    private GatewayUser translate(User user, Collection<Authority> authorities) {
        assert user != null;
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        GatewayUser gatewayUser = new GatewayUser(user.getName(),
                Objects.requireNonNull(passwordEncoderObjectProvider.getIfAvailable()).encode(user.getPassword()),
                Objects.equals(UserStatusEnum.NORMAL.getCode(), user.getStatus()),
                true,
                true,
                !Objects.equals(UserStatusEnum.LOCK.getCode(), user.getStatus()),
                grantedAuthorities
        );
        gatewayUser.setId(user.getId());
        gatewayUser.setPhone(user.getPhone());
        return gatewayUser;
    }

}
