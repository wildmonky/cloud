package org.lizhao.cloud.gateway.security.userdetailsservice;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.exception.CustomException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * description
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/7/21 14:49:32
 */
@Slf4j
public class DelegateReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final List<ReactiveUserDetailsService> reactiveUserDetailsServices = new ArrayList<>();

    private final List<ReactiveUserDetailsPasswordService> reactiveUserDetailsPasswordServices = new ArrayList<>();

    public DelegateReactiveUserDetailsServiceImpl(ReactiveUserDetailsService... reactiveUserDetailsServices) {

        List<ReactiveUserDetailsService> userDetailsServices = Arrays.stream(reactiveUserDetailsServices).filter(Objects::nonNull).toList();
        if (userDetailsServices.size() == 0) {
            throw new CustomException("无可用的用户信息服务");
        }
        for (ReactiveUserDetailsService userDetailsService : userDetailsServices) {
            this.reactiveUserDetailsServices.add(userDetailsService);
            if (userDetailsService instanceof ReactiveUserDetailsPasswordService) {
                this.reactiveUserDetailsPasswordServices.add((ReactiveUserDetailsPasswordService) userDetailsService);
            }
        }
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Flux.fromIterable(this.reactiveUserDetailsServices)
                .concatMap(service -> service.findByUsername(username))
                .next();
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return Flux.fromIterable(this.reactiveUserDetailsPasswordServices)
                .concatMap(service -> service.updatePassword(user, newPassword))
                .next();
    }

}
