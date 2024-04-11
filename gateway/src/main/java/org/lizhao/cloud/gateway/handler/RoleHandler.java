package org.lizhao.cloud.gateway.handler;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.repository.GroupRoleRelationRepository;
import org.lizhao.cloud.gateway.repository.UserRoleRelationRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 角色 handler
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:20
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class RoleHandler {

    @Resource
    private UserRoleRelationRepository userRoleRelationRepository;
    @Resource
    private GroupRoleRelationRepository groupRoleRelationRepository;

    /**
     * 绑定多个角色给单个用户
     * @param userRolesMap 用户-角色 map
     * @return 绑定的用户-角色关系
     */
    public Flux<UserRoleRelation> bindMoreToUser(Map<User, Collection<Role>> userRolesMap) {
        Publisher<UserRoleRelation> bound = CommonHandler.bind(userRolesMap,
                (user, role) -> {
                    if (user.getId() == null || role.getId() == null) {
                        return null;
                    }
                    UserRoleRelation relation = new UserRoleRelation();
                    relation.setUserId(user.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }, p -> userRoleRelationRepository.saveAll(Flux.from(p))
        );
        return Flux.from(bound);
    }

    /**
     * 绑定单个角色给多个用户
     * @param roleUsersMap 用户-角色 map
     * @return 绑定的用户-角色关系
     */
    public Flux<UserRoleRelation> bindToUsers(Map<Role, Collection<User>> roleUsersMap) {
        Publisher<UserRoleRelation> bound = CommonHandler.bind(roleUsersMap,
                (role, user) -> {
                    if (user.getId() == null || role.getId() == null) {
                        return null;
                    }
                    UserRoleRelation relation = new UserRoleRelation();
                    relation.setUserId(user.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }, p -> userRoleRelationRepository.saveAll(Flux.from(p))
        );
        return Flux.from(bound);
    }

    /**
     * 解除角色与用户之间的绑定关系
     * @param relationId 角色与用户之间的绑定关系
     */
    public Mono<Void> unbindFromUser(String relationId) {
        return userRoleRelationRepository.deleteById(relationId);
    }

    /**
     * 将多个角色绑定到一个组中
     * @param groupRolesMap 组-角色 map
     * @return 绑定的组-角色关系
     */
    public Flux<GroupRoleRelation> bindMoreToGroup(Map<Group, Collection<Role>> groupRolesMap) {
        Publisher<GroupRoleRelation> bound = CommonHandler.bind(groupRolesMap,
                (group, role) -> {
                    if (group.getId() == null || role.getId() == null) {
                        return null;
                    }
                    GroupRoleRelation relation = new GroupRoleRelation();
                    relation.setGroupId(group.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }, p -> groupRoleRelationRepository.saveAll(Flux.from(p)));

        return Flux.from(bound);
    }

    /**
     * 将单个角色绑定到多个组中
     * @param roleGroupsMap 组-角色 map
     * @return 绑定的组-角色关系
     */
    public Flux<GroupRoleRelation> bindToGroups(Map<Role, Collection<Group>> roleGroupsMap) {
        Publisher<GroupRoleRelation> bound = CommonHandler.bind(roleGroupsMap,
                (group, role) -> {
                    if (group.getId() == null || role.getId() == null) {
                        return null;
                    }
                    GroupRoleRelation relation = new GroupRoleRelation();
                    relation.setGroupId(group.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }, p -> groupRoleRelationRepository.saveAll(Flux.from(p)));

        return Flux.from(bound);
    }

    /**
     * 解除角色与组之间的绑定关系
     * @param relationId 角色与组之间的绑定关系
     */
    public Mono<Void> unbindFromGroup(String relationId) {
        return groupRoleRelationRepository.deleteById(relationId);
    }

}
