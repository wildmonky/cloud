package org.lizhao.cloud.gateway.security;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * Description 用户信息存入redis中，实现redis缓存
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:43
 * @since 0.0.1-SNAPSHOT
 */
public class RedisReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate;

    private DbReactiveUserDetailsService dbReactiveUserDetailsService;

    public RedisReactiveUserDetailsService(
            ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate,
            DbReactiveUserDetailsService dbReactiveUserDetailsService) {
        this.dbReactiveUserDetailsService = dbReactiveUserDetailsService;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return null;
    }
}
