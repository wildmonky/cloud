package org.lizhao.cloud.gateway.security.authentication;

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Description AuthenticationToken 转换
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-31 15:36
 * @since 0.0.1-SNAPSHOT
 */
public class RedisUserDetailsServiceReactiveAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    public RedisUserDetailsServiceReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return super.authenticate(authentication).map(TokenAuthenticationToken::new);
    }

}
