package org.lizhao.cloud.gateway.security.security.context.repository;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.utils.JwtUtils;
import org.lizhao.cloud.gateway.security.security.context.RedisSecurityContextImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description SecurityContext(包含用户信息，如：账号密码) 保存策略 Redis
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-28 20:24
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public class RedisSecurityContextRepository implements ServerSecurityContextRepository {

    @Value("${web.jwt.key}")
    private String jwtKey;

    @Resource
    private ReactiveRedisTemplate<String, Authentication> reactiveTokenRedisTemplate;

    /**
     * key pattern gateway_user:{jwt}
     */
    private String PREFIX = "gateway_user:";

    /**
     * 默认30分钟过期
     */
    private Duration expireDuration = Duration.of(30, ChronoUnit.MINUTES);

    /**
     * 每次获取时，更新过期时间
     * @param exchange 一次交互 获取request cookie中的token
     * @return Mono<SecurityContext>
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        HttpCookie token = exchange.getRequest().getCookies().getFirst("TOKEN");
        assert token != null;
        String tokenValue = token.getValue();
        assert StringUtils.isNotBlank(tokenValue);
        return reactiveTokenRedisTemplate.keys(PREFIX + tokenValue )
//                .take(1)
                .next()
                .flatMap(key ->
                        reactiveTokenRedisTemplate.opsForValue()
                                .getAndExpire(key, expireDuration)
                                .map(authentication -> new RedisSecurityContextImpl(authentication, tokenValue))
                );
    }

    /**
     * 保存 SecurityContext 信息 到 Redis中
     * @param exchange the exchange to associate to the SecurityContext
     * @param context the SecurityContext to save
     * @return
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        assert authentication != null;
        return reactiveTokenRedisTemplate.opsForValue().set(createKey(authentication), authentication, expireDuration)
                .flatMap(flag -> Mono.error(new Throwable("redis: token保存失败")));
    }

    /**
     * 删除 redis中的token 信息
     *
     * @param authentication 认证token
     * @return
     */
    public Mono<Boolean> remove(String token) {
        return reactiveTokenRedisTemplate.opsForValue().delete(token)
                .map(flag -> {
                    if (flag) {
                        log.info("移除TOKEN{}成功", token);
                    }
                    return flag;
                });
    }

    public String resetPrefix(String newPrefix) {
        this.PREFIX = newPrefix;
        return this.PREFIX;
    }

    public void resetDuration(Duration duration) {
        this.expireDuration = duration;
    }

    /**
     * pattern {PREFIX}:{jwtKey}
     * @param authentication
     * @return
     */
    private String createKey(Authentication authentication) {
        // 生成JWT
        Map<String, Object> map = new HashMap<>();
        map.put("credentials", authentication.getCredentials());
        map.put("details", authentication.getDetails());
        map.put("principal", authentication.getPrincipal());
        map.put("authorities", authentication.getAuthorities());
        String jwtToken = JwtUtils.generate(jwtKey, map, new Date(System.currentTimeMillis() + expireDuration.toMillis()));
        return PREFIX + jwtToken;
    }

}
