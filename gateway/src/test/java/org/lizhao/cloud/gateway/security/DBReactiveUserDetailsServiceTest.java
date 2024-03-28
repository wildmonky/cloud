package org.lizhao.cloud.gateway.security;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.security.userdetailsservice.DBReactiveUserDetailsService;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.Collections;

@SpringBootTest
class DBReactiveUserDetailsServiceTest {

    @Resource
    private DBReactiveUserDetailsService dbReactiveUserDetailsService;

    @Test
    void updatePassword() {
        GatewayUser gatewayUser = new GatewayUser("lizhao", "19960214", Collections.emptyList());
        gatewayUser.setPhone("18666496619");
        gatewayUser.setId("28785986637824");
        dbReactiveUserDetailsService.updatePassword(gatewayUser,"123456")
                .as(StepVerifier::create)
                .consumeNextWith(u -> System.out.println(u.getPassword()))
                .verifyComplete();
    }

    @Test
    void findByUsername() {
        dbReactiveUserDetailsService.findByUsername("lizhao")
                .as(StepVerifier::create)
                .consumeNextWith(user ->{
                    GatewayUser e = (GatewayUser) user;
                    for (Field declaredField : e.getClass().getDeclaredFields()) {
                        try {
                            declaredField.setAccessible(true);
                            System.out.println(declaredField.getName() + " : " + declaredField.get(e));
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).verifyComplete();
    }
}