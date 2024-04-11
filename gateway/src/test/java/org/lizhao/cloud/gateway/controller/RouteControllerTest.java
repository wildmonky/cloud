package org.lizhao.cloud.gateway.controller;

import com.alibaba.nacos.shaded.com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lizhao.cloud.gateway.configurer.WebFluxSecurityConfigurer;
import org.lizhao.cloud.gateway.configurer.properties.SecurityProperties;
import org.lizhao.cloud.gateway.model.predicateDefinition.PathPredicateDefinition;
import org.lizhao.cloud.gateway.serviceImpl.RouteServiceImpl;
import org.mockito.BDDMockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;


/**
 * Description 路由控制器测试
 *
 * @since 0.0.1-SNAPSHOT
 * @author lizhao
 * @date 2024/4/5 15:00
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(SecurityProperties.class)
@AutoConfigureDataR2dbc // 加载r2dbc配置类
@AutoConfigureDataRedis // 加载redis配置类
@AutoConfigureWebTestClient(timeout = "PT15M")
@WebFluxTest(controllers = org.lizhao.cloud.gateway.controller.RouteController.class)
@Import(WebFluxSecurityConfigurer.class)
class RouteControllerTest {

    @Resource
    private WebTestClient webTestClient;

    @MockBean
    private RouteServiceImpl routeServiceImpl;

    @Test
    void routeList() {
        BDDMockito.given(routeServiceImpl.search(null, null, null))
                .willReturn(Flux.just(new RouteDefinition()));
        webTestClient.get().uri("/route/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    void saveRouteList() {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setUri(URI.create("https://www.baidu.com"));
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");
        HashMap<String, String> map = new HashMap<>();
        map.put("Path", "/index");
        predicateDefinition.setArgs(map);
        PathPredicateDefinition pathPredicate = new PathPredicateDefinition(Sets.newHashSet("/**"));
        routeDefinition.setPredicates(Collections.singletonList(pathPredicate));

        BDDMockito.given(routeServiceImpl.save(Mono.just(routeDefinition)))
                .willReturn(Mono.empty());
        webTestClient.post().uri("/route/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(routeDefinition), RouteDefinition.class)
//                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    void removeRouteList() {
    }

    @Test
    void listTest() {
    }
}