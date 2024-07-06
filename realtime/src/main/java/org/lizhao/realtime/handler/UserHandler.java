package org.lizhao.realtime.handler;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-07-04 13:55
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class UserHandler {

    @Resource
    private RestTemplate userRestTemplate;

    @Resource
    private WebClient userServiceWebClient;

    /**
     * 根据 ids 查询对应的用户信息
     *
     * @param ids 用户id集合
     * @return 用户信息
     */
    public Collection<User> searchUsersByIds(Set<String> ids) {
//        RequestEntity<Set<String>> request = RequestEntity
//                .post("/user/ids")
//                .accept(MediaType.APPLICATION_JSON)
//                .body(ids);
//
//        ResponseEntity<Flux<User>> responseEntity = userRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
//
//        if (responseEntity.hasBody()) {
//            Flux<User> body = responseEntity.getBody();
//            return body.collectList().block();
//        }

        Mono<Collection<User>> users = userServiceWebClient.post()
                .uri("/user/ids")
                .body(Mono.just(ids), new ParameterizedTypeReference<HashSet<String>>(){})
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse ->
                        clientResponse
                                .bodyToMono(new ParameterizedTypeReference<ResponseBodyModel<Collection<User>>>(){})
                                .map(ResponseBodyModel::getResult)
                );

        Collection<User> userList = users.block();
        if (!ObjectUtils.isEmpty(userList)) {
            return userList;
        }

        return Collections.emptyList();
    }


}
