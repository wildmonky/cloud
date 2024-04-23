package org.lizhao.cloud.gateway.security.authentication;

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

/**
 * Description 外部应用 认证管理器 如：前端
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-31 15:36
 * @since 0.0.1-SNAPSHOT
 */
public class OutsideReactiveAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    public OutsideReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return super.authenticate(authentication).map(TokenAuthenticationToken::new);
    }

}
