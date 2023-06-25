package org.lizhao.cloud.gateway;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.cloud.gateway.controller.RouteController;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest
class GatewayApplicationTests {

//    @Resource
    private RouteController routeController;

    @Test
    void contextLoads() {
        System.out.println(routeController.listTest());
    }

    @Test
    public void test() {
        Pattern pattern = Pattern.compile(".*/test");
        Matcher matcher = pattern.matcher("/gateway/route/test");
        System.out.println(matcher.matches());
    }

}
