package org.lizhao.cloud.gateway.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.cloud.gateway.entity.user.Group;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class GroupRepositoryTest {

    @Resource
    private GroupRepository groupRepository;

    @Test
    public void test() {

        Group group = new Group();
        group.setName("lizhao");

        groupRepository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    System.out.println(e.toString());
                    return true;
                }).verifyComplete();
    }

}