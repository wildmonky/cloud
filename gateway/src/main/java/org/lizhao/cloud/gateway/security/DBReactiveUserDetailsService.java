package org.lizhao.cloud.gateway.security;

import org.lizhao.base.entity.user.UserInfo;
import org.lizhao.cloud.gateway.repository.UserInfoRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

/**
 * Description 用户信息存储在reactive 数据库中
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:15
 * @since 0.0.1-SNAPSHOT
 */
public class DBReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final UserInfoRepository repository;

    private final SecurityProperties securityProperties;

    private final ObjectProvider<PasswordEncoder> passwordEncoderObjectProvider;

    public DBReactiveUserDetailsService(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder, UserInfoRepository repository) {
        this.repository = repository;
        this.securityProperties = properties;
        this.passwordEncoderObjectProvider = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        UserDetails userDetails = User.withUserDetails(user)
                .password(newPassword)
                .build();
        return repository.save((UserInfo) userDetails).map(e -> e);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UserInfo user = new UserInfo();
        user.setUsername(username);
        return repository.findOne(Example.of(user)).map(e -> e);
    }

}
