package org.lizhao.cloud.gateway.security.authentication;

import org.lizhao.base.exception.MessageException;
import org.lizhao.cloud.gateway.handler.ResourceHandler;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.security.userdetailsservice.WebClientUserDetailsServiceImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * @author lizhao
 */
public class ExchangeReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final ResourceHandler resourceHandler;
    private final WebClientUserDetailsServiceImpl webClientUserDetailsServiceImpl;

    public ExchangeReactiveAuthorizationManager(ResourceHandler resourceHandler,
                                                WebClientUserDetailsServiceImpl webClientUserDetailsServiceImpl
    ) {
        this.resourceHandler = resourceHandler;
        this.webClientUserDetailsServiceImpl = webClientUserDetailsServiceImpl;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication.flatMap(auth -> {
            if (!auth.isAuthenticated()) {
                return Mono.error(new MessageException("当前用户未认证"));
            }
            GatewayUser gatewayUser = (GatewayUser)auth.getPrincipal();
            return Mono.just(gatewayUser.isAnonymous())
                    .flatMap(isAnonymous -> {
                        if (isAnonymous) {
                            return Mono.just(gatewayUser);
                        }

                        return webClientUserDetailsServiceImpl.findByUserId(gatewayUser.getId())
                                .switchIfEmpty(Mono.error(new MessageException("id{}对应的用户信息异常", gatewayUser.getId())));
                    })
                    .flatMap(userInfo ->
                            resourceHandler.hasAuthority(userInfo, context.getExchange().getRequest())
                                    .handle((has, sink) -> {
                                        if (has) {
                                            sink.next(new AuthorizationDecision(true));
                                            return;
                                        }
                                        sink.error(new MessageException("用户无权限操作"));
                                    })
                    );

        });
    }

//    @Override
//    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext context) {
//        // @formatter:off
//        return check(authentication, context)
//                .filter(AuthorizationDecision::isGranted)
//                .switchIfEmpty(Mono.defer(() -> Mono.error(new MessageException("当前用户无权限"))))
//                .flatMap((decision) -> Mono.empty());
//        // @formatter:on
//    }
}
