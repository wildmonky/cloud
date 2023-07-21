package org.lizhao.cloud.gateway.security;

import org.lizhao.base.exception.CustomException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * description
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/7/21 14:49:32
 */
public class ComposeReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final DBReactiveUserDetailsService dbReactiveUserDetailsService;

    private final RedisReactiveUserDetailsService redisReactiveUserDetailsService;

    public ComposeReactiveUserDetailsService(DBReactiveUserDetailsService dbReactiveUserDetailsService, RedisReactiveUserDetailsService redisReactiveUserDetailsService) {
        if (dbReactiveUserDetailsService == null && redisReactiveUserDetailsService == null) {
            throw new CustomException("用户信息服务最少有一个实现");
        }
        this.dbReactiveUserDetailsService = dbReactiveUserDetailsService;
        this.redisReactiveUserDetailsService = redisReactiveUserDetailsService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if (redisReactiveUserDetailsService != null && dbReactiveUserDetailsService == null) {
            return redisReactiveUserDetailsService.findByUsername(username);
        }
        if (redisReactiveUserDetailsService != null) {
            return redisReactiveUserDetailsService.findByUsername(username).switchIfEmpty(dbReactiveUserDetailsService.findByUsername(username));
        }
        return dbReactiveUserDetailsService.findByUsername(username);
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return null;
    }

}
