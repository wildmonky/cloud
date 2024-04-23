package org.lizhao.user.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.entity.user.Group;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

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

    @Test
    public void childTest() {
        groupRepository.child(Flux.just("1"))
                .as(StepVerifier::create)
                .consumeNextWith(group -> System.out.println(group.getName()))
                .consumeNextWith(group -> System.out.println(group.getName()))
                .verifyComplete();
    }

    @Test
    public void findGroupsIncludeChildByUserIdTest() {
        groupRepository.findGroupsIncludeChildByUserId("28785986637824")
                .as(StepVerifier::create)
                .thenConsumeWhile(Objects::nonNull, group ->
                    System.out.println(group.getName())
                )
                .verifyComplete();
    }

}