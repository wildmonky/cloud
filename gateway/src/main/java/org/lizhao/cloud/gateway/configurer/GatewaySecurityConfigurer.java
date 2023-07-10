package org.lizhao.cloud.gateway.configurer;

import org.lizhao.cloud.gateway.security.DbReactiveUserDetailsService;
import org.lizhao.cloud.gateway.security.RedisReactiveUserDetailsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description 网关安全配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:10
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GatewaySecurityConfigurer {

    @Bean
    public DbReactiveUserDetailsService reactiveUserDetailsService(SecurityProperties properties,
                                                                   ObjectProvider<PasswordEncoder> passwordEncoder) {
        return new DbReactiveUserDetailsService(properties, passwordEncoder);
    }

    @Bean
    @Primary
    public RedisReactiveUserDetailsService reactiveUserDetailsService(
            ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate,
            DbReactiveUserDetailsService dbReactiveUserDetailsService) {
        return new RedisReactiveUserDetailsService(reactiveRedisTemplate, dbReactiveUserDetailsService);
    }

}
