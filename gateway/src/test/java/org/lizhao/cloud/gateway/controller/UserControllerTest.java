package org.lizhao.cloud.gateway.controller;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lizhao.cloud.gateway.configurer.WebFluxSecurityConfigurer;
import org.lizhao.cloud.gateway.configurer.properties.SecurityProperties;
import org.lizhao.cloud.gateway.serviceImpl.UserServiceImpl;
import org.mockito.BDDMockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(SecurityProperties.class)
@AutoConfigureDataR2dbc // 加载r2dbc配置类
@AutoConfigureDataRedis // 加载redis配置类
@AutoConfigureWebTestClient(timeout = "PT15M")
@WebFluxTest(controllers = org.lizhao.cloud.gateway.controller.UserController.class)
@Import(WebFluxSecurityConfigurer.class)
class UserControllerTest {

    @Resource
    private WebTestClient webTestClient;

    @MockBean
    private UserServiceImpl userService;

    @Test
    void bindGroupToUser() {
        BDDMockito.given(userService.bindUsersToGroup(new HashMap<>()))
                .willReturn(Flux.empty());
        webTestClient.post().uri("/user/bind/to/group")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"{\\\"id\\\":\\\"1\\\"}\":[{\"id\":\"5516563788599296\"}]}")
//                .bodyValue("{\"{\\\"id\\\":\\\"1\\\"}\":[{\"id\":\"5516563788599296\"}]}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println);


    }
}