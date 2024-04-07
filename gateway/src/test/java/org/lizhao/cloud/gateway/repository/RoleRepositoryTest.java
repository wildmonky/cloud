package org.lizhao.cloud.gateway.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Objects;

@SpringBootTest
class RoleRepositoryTest {

    @Resource
    private RoleRepository roleRepository;

    @Test
    void allChild() {
        roleRepository.child(Mono.just("1"))
                .as(StepVerifier::create)
                .thenConsumeWhile(Objects::nonNull, this::printField)
                .verifyComplete();
    }

    private void printField(Object o) {
        for (Field declaredField : o.getClass().getDeclaredFields()) {
            try {
                declaredField.setAccessible(true);
                System.out.println(declaredField.getName() + " : " + declaredField.get(o));
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        for (Field declaredField : o.getClass().getSuperclass().getDeclaredFields()) {
            try {
                declaredField.setAccessible(true);
                System.out.println(declaredField.getName() + " : " + declaredField.get(o));
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}