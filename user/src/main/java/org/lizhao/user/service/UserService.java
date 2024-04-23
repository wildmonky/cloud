package org.lizhao.user.service;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.utils.BaseUtils;
import org.lizhao.user.handler.UserHandler;
import org.lizhao.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

/**
 * Description 用户服务 查找、下线
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-01 13:03
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class UserService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private UserHandler userHandler;
    @Resource
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public Flux<User> searchAll() {
        return userRepository.findAll();
    }

    public Mono<User> searchByName(String username) {
        return userRepository.findByName(username);
    }

    public Mono<User> searchDetailsByName(String username) {
        return userRepository.findByName(username);
    }

    public Mono<UserInfo> searchDetailsById(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException(userId + "用户不存在")))
                .map(user -> BaseUtils.copy(user, UserInfo.class))
                .flatMap(this::searchDetails);
    }

    public Mono<UserInfo> searchDetails(UserInfo user) {
        if (user == null || user.getId() == null) {
            return Mono.error(new RuntimeException("用户不存在"));
        }
        return Mono.just(user)
                .flatMap(userModel -> userHandler.userGroups(userModel.getId())
                        .collectList()
                        .defaultIfEmpty(Collections.emptyList())
                        .map(groups -> {
                            userModel.setGroups(new HashSet<>(groups));
                            return userModel;
                        })
                )
                .flatMap(userModel -> userHandler.userRoles(userModel.getId())
                        .collectList()
                        .defaultIfEmpty(Collections.emptyList())
                        .map(roles -> {
                            userModel.setRoles(new HashSet<>(roles));
                            return userModel;
                        })
                )
                .flatMap(userInfo -> userHandler.userAllAuthorities(userInfo)
                        .collectList()
                        .defaultIfEmpty(Collections.emptyList())
                        .map(authorities -> {
                            userInfo.setOriginAuthorities(new HashSet<>(authorities));
                            return userInfo;
                        })
                );
    }

    public Mono<User> save(User user) {
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<Void> remove(User user) {
        return userRepository.delete(user);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
