package org.lizhao.cloud.gateway.security;

import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.repository.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

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

    private UserRepository repository;

    private SecurityProperties securityProperties;

    private ObjectProvider<PasswordEncoder> passwordEncoderObjectProvider;

    public DBReactiveUserDetailsService(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder, UserRepository repository) {
        this.repository = repository;
        this.securityProperties = properties;
        this.passwordEncoderObjectProvider = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return repository.findOne(Example.of(user)).map(e -> e);
    }

}
