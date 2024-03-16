package org.lizhao.cloud.gateway.security;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
class ComposeReactiveUserDetailsServiceTest {

    @Resource
    private ComposeReactiveUserDetailsService composeReactiveUserDetailsService;

    @Test
    void findByUsername() {
        composeReactiveUserDetailsService.findByUsername("test")
                .subscribe(e -> {
                    for (Field field : e.getClass().getDeclaredFields()) {
                        try {
                            field.setAccessible(true);
                            System.out.println(field.getName() + ": " + field.get(e));
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
    }

    @Test
    void updatePassword() {
    }
}
