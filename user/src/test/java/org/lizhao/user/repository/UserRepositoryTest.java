package org.lizhao.user.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.base.entity.user.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClientExtensionsKt;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.Objects;

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

    @Test
    void findByUsername() {
        User user = new User();
        user.setName("lizhao");

        userRepository.findAll()
                .as(StepVerifier::create)
                .consumeNextWith(e -> {
                    System.out.println(e.toString());
                }).verifyComplete();
    }

    @Test
    public void save() {
        SnowFlake snowFlake = new SnowFlake(1L, 1L, 0L);
        User user = new User();
//        user.setId(String.valueOf(snowFlake.generateNextId()));
        user.setName("lizhao");
        user.setPhone("18666496619");
        user.setPassword("19960214");
        userRepository.save(user)
                .as(StepVerifier::create)
                .thenConsumeWhile(Objects::nonNull, e -> {
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