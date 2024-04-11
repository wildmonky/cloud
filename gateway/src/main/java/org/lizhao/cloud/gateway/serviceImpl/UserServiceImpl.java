package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.handler.UserHandler;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.repository.UserRepository;
import org.lizhao.cloud.gateway.security.context.repository.RedisSecurityContextRepository;
import org.lizhao.cloud.gateway.security.event.UserOfflineEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
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
public class UserServiceImpl implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private RedisSecurityContextRepository repository;
    @Resource
    private UserHandler userHandler;
    @Resource
    private UserRepository userRepository;

    public Flux<User> searchAll() {
        return userRepository.findAll();
    }

    /**
     * 获取在线用户
     * @return 在线用户
     */
    public Flux<GatewayUser> onlineUser(String token) {
        return repository.onlineUser(token);
    }

    public Mono<Boolean> offline(String token) {
        applicationEventPublisher.publishEvent(new UserOfflineEvent(this));
        return repository.remove(token);
    }

    public Mono<User> save(User user) {
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
