package org.lizhao.cloud.gateway.controller;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lizhao.cloud.gateway.configurer.properties.SecurityProperties;
import org.lizhao.cloud.gateway.serviceImpl.RouteServiceImpl;
import org.mockito.BDDMockito;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(SecurityProperties.class)
@AutoConfigureDataR2dbc // 加载r2dbc配置类
@AutoConfigureDataRedis // 加载redis配置类
@WebFluxTest(controllers = org.lizhao.cloud.gateway.controller.RouteController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                ManagementWebSecurityAutoConfiguration.class
})
class RouteControllerTest {

    @Resource
    private WebTestClient webTestClient;

    @MockBean
    private RouteServiceImpl routeServiceImpl;

    @Test
    void routeList() {
        BDDMockito.given(routeServiceImpl.search(null, null, null))
                .willReturn(Mono.just(Collections.singletonList(new RouteDefinition())));
        webTestClient.get().uri("/route/list")
//                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    void saveRouteList() {
    }

    @Test
    void removeRouteList() {
    }

    @Test
    void listTest() {
    }
}