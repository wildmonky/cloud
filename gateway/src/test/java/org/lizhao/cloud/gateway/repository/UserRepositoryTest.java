package org.lizhao.cloud.gateway.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.cloud.gateway.entity.user.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;

/**
 * Description UserInfoRepository 单元测试
 *
 * @since 0.0.1-SNAPSHOT
 * @author lizhao
 * @date 2024/3/18 22:00
 */
@SpringBootTest
class UserRepositoryTest {

    @Resource
    private UserRepository userRepository;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void findByUsername() {
        User user = new User();
        user.setName("lizhao");

        userRepository.findAll()
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    System.out.println(e.toString());
                    return true;
                }).verifyComplete();
    }

    @Test
    public void save() {
        SnowFlake snowFlake = new SnowFlake(1L, 1L, 0L);
        User user = new User();
        user.setId(String.valueOf(snowFlake.generateNextId()));
        user.setName("lizhao");
        user.setPhone("18666496619");
        user.setPassword("19960214");
        userRepository.save(user)
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    for (Field declaredField : e.getClass().getDeclaredFields()) {
                        try {
                            declaredField.setAccessible(true);
                            System.out.println(declaredField.getName() + " : " + declaredField.get(e));
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    return true;
                }).verifyComplete();
    }

}