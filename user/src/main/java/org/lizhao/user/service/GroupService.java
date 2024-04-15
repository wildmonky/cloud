package org.lizhao.user.service;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.user.handler.GroupHandler;
import org.lizhao.user.model.UserGroupModel;
import org.lizhao.user.repository.GroupRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description 用户组服务
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-08 0:00
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class GroupService {

    @Resource
    private GroupRepository groupRepository;
    @Resource
    private GroupHandler groupHandler;

    public Flux<Group> searchAll() {
        return groupRepository.findAll();
    }

    public Mono<List<Group>> searchTree() {
        return groupRepository.findAll().collectList().map(coll -> groupHandler.generateTree(coll));
    }

    public Flux<UserGroupModel> searchBoundUsers(String groupId) {
        return groupHandler.findUsersInGroup(groupId);
    }

    public Flux<User> searchUnboundUsers(String groupId) {
        return groupHandler.findUsersNotInGroup(groupId);
    }

    public Mono<Group> save(Group group) {
        return groupRepository.save(group);
    }

    public Mono<Void> remove(Group group) {
        return groupRepository.delete(group);
    }

}
