package org.lizhao.cloud.gateway.security.userdetailsservice;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description 用户信息存入redis中，实现redis缓存
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:43
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class RedisReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final ReactiveRedisTemplate<String, UserDetails> reactiveUserRedisTemplate;

    /**
     * key pattern gateway_user:{username}:{jwt}
     */
    private String PREFIX = "gateway_user:";

    @Value("${web.jwt.key}")
    private String jwtKey;

    /**
     * 默认30分钟过期
     */
    private Duration expireDuration = Duration.of(30, ChronoUnit.MINUTES);

    public RedisReactiveUserDetailsService(ReactiveRedisTemplate<String, UserDetails> reactiveUserRedisTemplate) {
        this.reactiveUserRedisTemplate = reactiveUserRedisTemplate;
    }

    /**
     * 直接删除 user 记录
     * @param user the user to modify the password for
     * @param newPassword the password to change to
     * @return
     */
    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return reactiveUserRedisTemplate.opsForValue().delete(createKey(user))
                .doOnSuccess(flag -> log.info("用户{}删除成功", user.getPassword()))
                .doOnError(flag -> log.error("用户{}删除失败", user.getUsername()))
                .as(flag -> Mono.empty());
    }

    /**
     * 每次获取时，更新过期时间
     * @param username the username to look up
     * @return
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return reactiveUserRedisTemplate.keys(PREFIX + ":" + username + ":*")
                .take(1)
                .next()
                .flatMap(key ->
                    reactiveUserRedisTemplate.opsForValue().getAndExpire(key, expireDuration)
                );
    }

    /**
     * 保存 user 信息
     * @param userDetails
     * @return
     */
    public Mono<Boolean> save(UserDetails userDetails) {
        return reactiveUserRedisTemplate.opsForValue().set(createKey(userDetails), userDetails, expireDuration);
    }

    /**
     * 删除 user 信息
     *
     * @param userDetails
     * @return
     */
    public Mono<Boolean> remove(UserDetails userDetails) {
        return reactiveUserRedisTemplate.opsForValue().delete(createKey(userDetails));
    }

    public String resetPrefix(String newPrefix) {
        this.PREFIX = newPrefix;
        return this.PREFIX;
    }

    public void resetDuration(Duration duration) {
        this.expireDuration = duration;
    }

    /**
     * pattern {PREFIX}:{username}:{jwtKey}
     * @param userDetails
     * @return
     */
    private String createKey(UserDetails userDetails) {
        // 生成JWT
        Map<String, Object> map = new HashMap<>();
        map.put("name", userDetails.getUsername());
        map.put("password", userDetails.getPassword());
        String jwtToken = JwtUtils.generate(jwtKey, map, new Date(System.currentTimeMillis() + expireDuration.toMillis()));

        return PREFIX + userDetails.getUsername() + ":" + jwtToken;
    }

}
