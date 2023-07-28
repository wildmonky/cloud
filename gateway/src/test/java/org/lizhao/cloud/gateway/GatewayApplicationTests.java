package org.lizhao.cloud.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.utils.JwtUtils;
import org.lizhao.cloud.gateway.controller.RouteController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.server.authentication.AnonymousAuthenticationWebFilter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class GatewayApplicationTests {

    @Resource
    private RouteController routeController;
    @Value("${web.jwt.key}")
    private byte[] jwtKey;

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

    @Test
    public void test1() {
        Pattern pattern = Pattern.compile(".*/test");
        Matcher matcher = pattern.matcher("/gateway/route/test");
        System.out.println(matcher.matches());
    }

    /**
     * JwtUtils 测试
     */
    @Test
    public void test2() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("lizhao", "19960214");
        String jws = JwtUtils.generate(jwtKey, map, Date.valueOf(LocalDateTime.now().plusDays(1).toLocalDate()));
        System.out.println(jws);
        Jws<Claims> parse = JwtUtils.parse(jwtKey, jws);
        System.out.println(parse.getHeader().toString());
        System.out.println(parse.getBody().toString());
        System.out.println(parse.getSignature());
    }

}
