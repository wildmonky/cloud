package org.lizhao.cloud.gateway.handler;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupAuthorityRelation;
import org.lizhao.base.entity.relation.RoleAuthorityRelation;
import org.lizhao.base.entity.relation.UserAuthorityRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.repository.*;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 权限 处理器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:32
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class AuthorityHandler {

    @Resource
    private GroupAuthorityRelationRepository groupAuthorityRelationRepository;
    @Resource
    private UserAuthorityRelationRepository userAuthorityRelationRepository;
    @Resource
    private RoleAuthorityRelationRepository roleAuthorityRelationRepository;
    @Resource
    private AuthorityRepository authorityRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private GroupRepository groupRepository;

    /**
     * 绑定角色-权限关系
     *
     * @param roleAuthorityMap Role-Authority 对应关系
     * @return 绑定的 角色-权限关系
     */
    public Flux<RoleAuthorityRelation> bindToRole(Map<Role, Collection<Authority>> roleAuthorityMap) {
        Publisher<RoleAuthorityRelation> publisher = CommonHandler.bind(roleAuthorityMap,
                (role, authority) -> {
                    if (role.getId() == null || authority.getId() == null) {
                        return null;
                    }
                    RoleAuthorityRelation relation = new RoleAuthorityRelation();
                    relation.setRoleId(role.getId());
                    relation.setAuthorityId(authority.getId());
                    return relation;
                },
                p -> roleAuthorityRelationRepository.saveAll(Flux.from(p))
        );
        return Flux.from(publisher);
    }

    /**
     * 将权限与用户解绑
     * @param relation 权限-用户绑定关系
     */
    public Mono<Void> unbindFromUser(UserAuthorityRelation relation) {
        return userAuthorityRelationRepository.delete(relation);
    }

    /**
     * 绑定组-权限关系
     *
     * @param groupAuthorityMap Group-Authority 对应关系
     * @return 绑定的 组-权限关系
     */
    public Flux<GroupAuthorityRelation> bindToGroup(Map<Group, Collection<Authority>> groupAuthorityMap) {
        Publisher<GroupAuthorityRelation> publisher = CommonHandler.bind(groupAuthorityMap,
                (group, authority) -> {
                    if (group.getId() == null || authority.getId() == null) {
                        return null;
                    }
                    GroupAuthorityRelation relation = new GroupAuthorityRelation();
                    relation.setGroupId(group.getId());
                    relation.setAuthorityId(authority.getId());
                    return relation;
                },
                p -> groupAuthorityRelationRepository.saveAll(Flux.from(p))
        );
        return Flux.from(publisher);
    }

    /**
     * 将权限与组解绑
     * @param relation 权限-组绑定关系
     */
    public Mono<Void> unbindFromGroup(GroupAuthorityRelation relation) {
        return groupAuthorityRelationRepository.delete(relation);
    }

    /**
     * 绑定用户-权限关系
     *
     * @param userAuthorityMap User-Authority 对应关系
     * @return 绑定的 用户-权限关系
     */
    public Flux<UserAuthorityRelation> bindToUser(Map<User, Collection<Authority>> userAuthorityMap) {
        Publisher<UserAuthorityRelation> publisher = CommonHandler.bind(userAuthorityMap,
                (user, authority) -> {
                    if (user.getId() == null || authority.getId() == null) {
                        return null;
                    }
                    UserAuthorityRelation relation = new UserAuthorityRelation();
                    relation.setUserId(user.getId());
                    relation.setAuthorityId(authority.getId());
                    return relation;
                },
                p -> userAuthorityRelationRepository.saveAll(Flux.from(p))
        );
        return Flux.from(publisher);
    }

    /**
     * 将权限与角色解绑
     * @param relation 权限-角色绑定关系
     */
    public Mono<Void> unbindFromRole(RoleAuthorityRelation relation) {
        return roleAuthorityRelationRepository.delete(relation);
    }

    public Flux<User> boundUsers(String authorityId) {
        return userRepository.findUsersByAuthorityId(authorityId);
    }

    public Flux<Group> boundGroups(String authorityId) {
        return groupRepository.findGroupsByAuthorityId(authorityId);
    }

    public Flux<Role> boundRoles(String authorityId) {
        return roleRepository.findRolesByAuthorityId(authorityId);
    }

}
