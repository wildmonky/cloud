package org.lizhao.cloud.gateway.security;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
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

    private final ReactiveRedisTemplate<String, String> reactiveStringRedisTemplate;

    private final Gson gson = new Gson();

    public RedisReactiveUserDetailsService(ReactiveRedisTemplate<String, String> reactiveStringRedisTemplate) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return reactiveStringRedisTemplate.hasKey(username).flatMap(flag -> {
            if (flag) {
                return reactiveStringRedisTemplate.opsForValue().get(username).map(str -> gson.fromJson(str, UserDetails.class));
            }
            return Mono.empty();
        });
    }
}
