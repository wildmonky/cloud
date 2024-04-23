package org.lizhao.user.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.exception.MessageException;
import org.lizhao.user.handler.GroupHandler;
import org.lizhao.user.model.UserGroupModel;
import org.lizhao.user.repository.GroupRepository;
import org.lizhao.user.repository.GroupUserRelationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description 用户组服务
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-08 0:00
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
public class GroupService {

    @Resource
    private GroupRepository groupRepository;
    @Resource
    private GroupUserRelationRepository groupUserRelationRepository;
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
        return groupRepository.existsAllByParentId(group.getId())
                .flatMap(flag -> {
                    if (flag) {
                        return Mono.error(new MessageException("{}中存在子组，无法直接删除", group.getName()));
                    }
                    return groupUserRelationRepository.existsAllByGroupId(group.getId());
                })
                .flatMap(flag -> {
                    if (flag) {
                        return Mono.error(new MessageException("{}中存在用户，无法直接删除", group.getName()));
                    }
                    return groupRepository.delete(group);
                });
    }

    public Flux<GroupUserRelation> bindUserToGroups(Map<User, Collection<Group>> map) {
        return groupHandler.bindToGroups(map);
    }

    public Flux<GroupUserRelation> bindUsersToGroup(Map<Group, Collection<User>> map) {
        return groupHandler.bindMoreToGroup(map);
    }

    public Mono<Void> unbindFromGroup(GroupUserRelation relation) {
        return groupHandler.unbindFromGroup(relation);
    }


}
