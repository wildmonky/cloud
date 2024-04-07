package org.lizhao.cloud.gateway.handler;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.entity.user.User;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.Objects;

@SpringBootTest
class UserHandlerTest {

    @Resource
    private UserHandler userHandler;

    @Test
    void userAllAuthorities() {
        User user = new User();
        user.setId("28785986637824");
        userHandler.userAllAuthorities(user)
                .as(StepVerifier::create)
                .thenConsumeWhile(Objects::nonNull, authority -> System.out.println(authority.getName()))
                .verifyComplete()
        ;
    }

    @Test
    void userGroupsIncludeChild() {
    }

    @Test
    void userGroups() {
    }

    @Test
    void userRoles() {
    }

    @Test
    void roleAuthorities() {
    }

    @Test
    void groupAuthorities() {
    }

    @Test
    void groupRoles() {
    }

    @Test
    void treeGroup() {
    }

    @Test
    void treeRole() {
    }
}