package org.lizhao.user.service;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
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

    public Mono<User> save(User user) {
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<Void> remove(User user) {
        return userRepository.delete(user);
    }

    public Flux<GroupUserRelation> bindUserToGroups(Map<User, Collection<Group>> map) {
        return userHandler.bindToGroups(map);
    }

    public Flux<GroupUserRelation> bindUsersToGroup(Map<Group, Collection<User>> map) {
        return userHandler.bindMoreToGroup(map);
    }

    public Mono<Void> unbindFromGroup(GroupUserRelation relation) {
        return userHandler.unbindFromGroup(relation);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
