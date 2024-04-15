package org.lizhao.cloud.gateway.security.userdetailsservice;

import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * Description 从 用户服务 检索用户
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 21:59
 * @since 0.0.1-SNAPSHOT
 */
public class WebClientUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final WebClient userServiceWebClient;

    public WebClientUserDetailsServiceImpl(WebClient userServiceWebClient) {
        this.userServiceWebClient = userServiceWebClient;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userServiceWebClient.get()
                .uri("/user/search/" + username)
                .exchangeToMono(clientResponse ->
                        clientResponse
                                .bodyToMono(new ParameterizedTypeReference<ResponseBodyModel<User>>(){})
                                .map(responseBodyModel -> {
                                    User user = responseBodyModel.getResult();
                                    return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), Collections.emptyList());
                                })
                );
    }

}
