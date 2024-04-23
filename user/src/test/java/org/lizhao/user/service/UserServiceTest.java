package org.lizhao.user.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
    @Test
    void searchByName() {
        userService.searchByName("lizhao")
                .as(StepVerifier::create)
                .consumeNextWith(e ->System.out.println(e.toString()))
                .verifyComplete();
    }
}