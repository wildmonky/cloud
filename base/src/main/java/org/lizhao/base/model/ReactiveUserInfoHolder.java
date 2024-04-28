package org.lizhao.base.model;

import org.lizhao.base.exception.MessageException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.function.Function;

/**
 * Description 响应式用户信息持有器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-21 18:27
 * @since 0.0.1-SNAPSHOT
 * @see org.springframework.security.core.context.ReactiveSecurityContextHolder
 */
public final class ReactiveUserInfoHolder {

    private static final Class<?> USER_INFO_KEY = UserInfo.class;
    public static Mono<UserInfo> get() {
        return Mono.deferContextual(Mono::just)
                .cast(Context.class)
                .filter(ReactiveUserInfoHolder::hasUserInfo)
                .flatMap(ReactiveUserInfoHolder::getUserInfo);
    }

    private static boolean hasUserInfo(Context context) {
        return context.hasKey(USER_INFO_KEY);
    }

    private static Mono<UserInfo> getUserInfo(Context context) {
        return context.<Mono<UserInfo>>get(USER_INFO_KEY).switchIfEmpty(Mono.error(new MessageException("用户信息为空")));
    }

    public static Function<Context, Context> clear() {
        return (context) -> context.delete(USER_INFO_KEY);
    }

    /**
     * 返回 Context，要结合{@link Mono#contextWrite} 或者 {@link Flux#contextWrite}
     * @param userInfo 用户信息
     * @return 设置了用户信息的Context
     */
    public static ContextView withUserInfo(UserInfo userInfo) {
        return withMonoUserInfo(Mono.just(userInfo));
    }

    public static ContextView withMonoUserInfo(Mono<UserInfo> userInfoMono) {
        return Context.of(USER_INFO_KEY, userInfoMono);
    }

}
