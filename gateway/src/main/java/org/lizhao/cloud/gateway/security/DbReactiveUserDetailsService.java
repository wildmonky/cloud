package org.lizhao.cloud.gateway.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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
public class DbReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private SecurityProperties securityProperties;

    private ObjectProvider<PasswordEncoder> passwordEncoderObjectProvider;

    public DbReactiveUserDetailsService(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder) {
        this.securityProperties = properties;
        this.passwordEncoderObjectProvider = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return Mono.just(User.withUserDetails(user).passwordEncoder(password -> {
            PasswordEncoder passwordEncoder = this.passwordEncoderObjectProvider.getIfAvailable();
            return passwordEncoder == null ? password : passwordEncoder.encode(password);
        }).password(newPassword).build());
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return null;
    }

}
