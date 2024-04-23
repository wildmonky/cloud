package org.lizhao.cloud.gateway.security.context.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.utils.JwtUtils;
import org.lizhao.cloud.gateway.configurer.properties.SecurityProperties;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.security.authentication.TokenAuthenticationToken;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;

/**
 * Description SecurityContext(包含用户信息，如：账号密码) 保存策略 Redis
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-28 20:24
 * @since 0.0.1-SNAPSHOT
 * @see org.springframework.security.web.server.context.ReactorContextWebFilter
 */
@Slf4j
public class RedisSecurityContextRepository implements ServerSecurityContextRepository {

    private final SecurityProperties securityProperties;

    private final ReactiveRedisTemplate<String, TokenAuthenticationToken> reactiveTokenRedisTemplate;

    /**
     * key pattern gateway_user:{jwt}
     */
    private String prefix = "gateway_user:";

    /**
     * 默认30分钟过期
     */
    private Duration expireDuration;

    public RedisSecurityContextRepository(SecurityProperties securityProperties,
                                          ReactiveRedisTemplate<String, TokenAuthenticationToken> reactiveTokenRedisTemplate) {
        this.securityProperties = securityProperties;
        this.reactiveTokenRedisTemplate = reactiveTokenRedisTemplate;
        this.expireDuration = Duration.ofSeconds(securityProperties.getJwt().getMaxAge());
    }

    /**
     * 每次获取时，更新过期时间
     * 注意：不能返回 Mono.just(new SecurityContextImpl())
     * {@link SpringTemplateEngine} 会通过 {@link SpringStandardDialect#REACTIVE_MODEL_ADDITIONS_WEB_EXCHANGE_PRINCIPAL_NAME}
     * 去加载 {@link ServerWebExchange#getPrincipal()} 从而导致空指针异常
     *
     * @param exchange 一次交互 获取request cookie中的token
     * @return Mono<SecurityContext>
     * @see ThymeleafReactiveView#render
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String tokenValue = exchange.getRequest().getHeaders().getFirst(securityProperties.getAuth().getHeaderName());
        if (StringUtils.isBlank(tokenValue)) {
            return  Mono.empty();
        }
        assert StringUtils.isNotBlank(tokenValue);
        return reactiveTokenRedisTemplate.keys(tokenValue)
//                .take(1)
                .next()
                .flatMap(key ->
                        reactiveTokenRedisTemplate.opsForValue()
                                .getAndExpire(key, expireDuration)
                                .map(SecurityContextImpl::new)
                );
    }

    /**
     * 保存 SecurityContext 信息到 Redis中，并将token写入 response 中，注意跨域
     * @param exchange the exchange to associate to the SecurityContext
     * @param context the SecurityContext to save
     * @return
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        String cookieDomain = exchange.getRequest().getHeaders().getFirst("Host");
        Authentication authentication = context.getAuthentication();
        assert authentication != null;
        String token = createKey(this.prefix, authentication);
        TokenAuthenticationToken authenticationToken = authentication instanceof TokenAuthenticationToken ?
                (TokenAuthenticationToken)authentication : new TokenAuthenticationToken(authentication);
        authenticationToken.setToken(token);
        return reactiveTokenRedisTemplate.opsForValue().set(token, authenticationToken, expireDuration)
                .doOnNext(redisSaveFlag -> {
                    if (redisSaveFlag) {
                        log.info("redis: ACCESS-TOKEN 保存成功");
                    } else {
                        log.error("redis: ACCESS-TOKEN 保存失败");
                    }
                }).doOnNext(redisSaveFlag -> {
                    if (redisSaveFlag) {
                        exchange.getResponse().getCookies()
                                .set(securityProperties.getAuth().getCookieName(),
                                        ResponseCookie.from(securityProperties.getAuth().getCookieName(), token)
                                                .maxAge(securityProperties.getJwt().getMaxAge())
                                                .domain(cookieDomain) // 设置cookie的 domain 与跳转请求相同
                                                .path("/")
//                                                .httpOnly(true)
                                                .build()
                                );
                        log.info("response cookie: ACCESS-TOKEN 保存成功");
                    }
                }).then();
    }

    /**
     * 获取在线用户
     * @return 在线用户
     */
    public Flux<GatewayUser> onlineUser(String token) {
        return reactiveTokenRedisTemplate.keys(StringUtils.isNotEmpty(token) ? token : prefix + "**")
                .flatMap(key -> reactiveTokenRedisTemplate.opsForValue().get(key))
                .handle((authenticationToken, sink) -> {
                    GatewayUser gatewayUser = (GatewayUser) authenticationToken.getPrincipal();
                    gatewayUser.setToken(authenticationToken.getToken());
                    sink.next(gatewayUser);
                });
    }

    /**
     * 删除 redis中的token 信息
     *
     * @param token 认证token
     * @return
     */
    public Mono<Boolean> remove(String token) {
        return reactiveTokenRedisTemplate.opsForValue().delete(token)
                .map(flag -> {
                    if (flag) {
                        log.info("redis移除TOKEN成功: {}", token);
                    } else {
                        log.error("redis移除TOKEN失败: {}", token);
                    }
                    return flag;
                });
    }

    public String resetPrefix(String newPrefix) {
        this.prefix = newPrefix;
        return this.prefix;
    }

    public void resetDuration(Duration duration) {
        this.expireDuration = duration;
    }

    /**
     * pattern {PREFIX}:{jwtKey}
     * @param prefix 前缀
     * @param authentication 认证信息
     * @return 生成的json web token
     */
    private String createKey(String prefix, Authentication authentication) {
        // 生成JWT
        Map<String, Object> map = new HashMap<>();
        map.put("credentials", authentication.getCredentials());
        map.put("details", authentication.getDetails());
        map.put("principal", authentication.getPrincipal());
        map.put("authorities", authentication.getAuthorities());
        String jwtToken = JwtUtils.generate(Objects.requireNonNull(securityProperties.getJwt()).getKey(), map, new Date(System.currentTimeMillis() + expireDuration.toMillis()));
        return prefix + jwtToken;
    }

    private Map<String, String> castToMap(Object o) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        for (Field declaredField : o.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            map.put(declaredField.getName(), (String) declaredField.get(o));
        }
        return map;
    }

}
