package org.lizhao.cloud.gateway;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.cloud.gateway.logic.controller.RouteController;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatewayApplicationTests {

    @Resource
    private RouteController routeController;

    @Test
    void contextLoads() {
        System.out.println(routeController.listTest());
    }

}
