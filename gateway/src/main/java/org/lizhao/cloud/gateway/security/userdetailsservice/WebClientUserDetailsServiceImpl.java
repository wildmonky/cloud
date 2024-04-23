package org.lizhao.cloud.gateway.security.userdetailsservice;

import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.model.ResponseBodyModel;
import org.lizhao.base.model.UserInfo;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        if (StringUtils.isBlank(username)) {
            return Mono.error(new Throwable("用户名为空"));
        }
        return userServiceWebClient.get()
                .uri("/user/" + username)
                .exchangeToMono(clientResponse ->
                        clientResponse
                                .bodyToMono(new ParameterizedTypeReference<ResponseBodyModel<UserInfo>>(){})
                                .map(responseBodyModel -> {
                                    UserInfo user = responseBodyModel.getResult();
                                    return new GatewayUser(user);
                                })
                );
    }

    public Mono<UserInfo> findByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Mono.error(new Throwable("用户Id为空"));
        }
        return userServiceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/user/")
                            .queryParam("id", userId)
                            .build()
                )
                .exchangeToMono(clientResponse ->
                        clientResponse
                                .bodyToMono(new ParameterizedTypeReference<ResponseBodyModel<UserInfo>>(){})
                                .handle((responseBodyModel, sink) -> {
                                    UserInfo user = responseBodyModel.getResult();
                                    if (user == null) {
                                        sink.error(new RuntimeException("找不到id为" + userId + "的用户"));
                                        return;
                                    }
                                    sink.next(user);
                                })
                );
    }

}
