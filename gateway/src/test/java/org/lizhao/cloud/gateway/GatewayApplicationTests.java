package org.lizhao.cloud.gateway;

import org.junit.jupiter.api.Test;
import org.lizhao.cloud.gateway.controller.RouteController;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class GatewayApplicationTests {

    @Resource
    private RouteController routeController;

    @Test
    void contextLoads() {
        System.out.println(routeController.listTest());
    }

}
