package org.lizhao.user.handler;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupAuthorityRelation;
import org.lizhao.base.entity.relation.RoleAuthorityRelation;
import org.lizhao.base.entity.relation.UserAuthorityRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.user.model.GroupAuthorityModel;
import org.lizhao.user.model.RoleAuthorityModel;
import org.lizhao.user.model.UserAuthorityModel;
import org.lizhao.user.repository.*;
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
    public Flux<RoleAuthorityRelation> bindMoreToRole(Map<Role, Collection<Authority>> roleAuthorityMap) {
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
     * 绑定角色-权限关系
     *
     * @param roleAuthorityMap Role-Authority 对应关系
     * @return 绑定的 角色-权限关系
     */
    public Flux<RoleAuthorityRelation> bindToRoles(Map<Authority, Collection<Role>> roleAuthorityMap) {
        Publisher<RoleAuthorityRelation> publisher = CommonHandler.bind(roleAuthorityMap,
                (authority, role) -> {
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
        if (relation == null) {
            return Mono.error(new Throwable("绑定关系为空"));
        }
        if (relation.getAuthorityId() == null || relation.getUserId() == null) {
            return Mono.error(new Throwable("异常的绑定关系"));
        }
        return userAuthorityRelationRepository.findByUserIdAndAuthorityId(relation.getUserId(), relation.getAuthorityId())
                .switchIfEmpty(Mono.error(new Throwable("不存在对应的绑定关系")))
                .collectList()
                .flatMap(l -> {
                    if (l.size() > 1) {
                        return Mono.error(new Throwable("存在多个相同的绑定关系"));
                    }
                    return userAuthorityRelationRepository.deleteById(l.get(0).getId());
                });
    }

    /**
     * 绑定组-权限关系
     *
     * @param groupAuthorityMap Group-Authority 对应关系
     * @return 绑定的 组-权限关系
     */
    public Flux<GroupAuthorityRelation> bindMoreToGroup(Map<Group, Collection<Authority>> groupAuthorityMap) {
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
     * 绑定组-权限关系
     *
     * @param groupAuthorityMap Group-Authority 对应关系
     * @return 绑定的 组-权限关系
     */
    public Flux<GroupAuthorityRelation> bindToGroups(Map<Authority, Collection<Group>> groupAuthorityMap) {
        Publisher<GroupAuthorityRelation> publisher = CommonHandler.bind(groupAuthorityMap,
                (authority, group) -> {
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
        if (relation == null) {
            return Mono.error(new Throwable("绑定关系为空"));
        }
        if (relation.getAuthorityId() == null || relation.getGroupId() == null) {
            return Mono.error(new Throwable("异常的绑定关系"));
        }
        return groupAuthorityRelationRepository.findByGroupIdAndAuthorityId(relation.getGroupId(), relation.getAuthorityId())
                .switchIfEmpty(Mono.error(new Throwable("不存在对应的绑定关系")))
                .collectList()
                .flatMap(l -> {
                    if (l.size() > 1) {
                        return Mono.error(new Throwable("存在多个相同的绑定关系"));
                    }
                    return groupAuthorityRelationRepository.deleteById(l.get(0).getId());
                });
    }

    /**
     * 绑定用户-权限关系
     *
     * @param userAuthorityMap User-Authority 对应关系
     * @return 绑定的 用户-权限关系
     */
    public Flux<UserAuthorityRelation> bindMoreToUser(Map<User, Collection<Authority>> userAuthorityMap) {
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
     * 绑定用户-权限关系
     *
     * @param userAuthorityMap User-Authority 对应关系
     * @return 绑定的 用户-权限关系
     */
    public Flux<UserAuthorityRelation> bindToUsers(Map<Authority, Collection<User>> userAuthorityMap) {
        Publisher<UserAuthorityRelation> publisher = CommonHandler.bind(userAuthorityMap,
                (authority, user) -> {
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
        if (relation == null) {
            return Mono.error(new Throwable("绑定关系为空"));
        }
        if (relation.getAuthorityId() == null || relation.getRoleId() == null) {
            return Mono.error(new Throwable("异常的绑定关系"));
        }
        return roleAuthorityRelationRepository.findByRoleIdAndAuthorityId(relation.getRoleId(), relation.getAuthorityId())
                .switchIfEmpty(Mono.error(new Throwable("不存在对应的绑定关系")))
                .collectList()
                .flatMap(l -> {
                    if (l.size() > 1) {
                        return Mono.error(new Throwable("存在多个相同的绑定关系"));
                    }
                    return roleAuthorityRelationRepository.deleteById(l.get(0).getId());
                });
    }

    public Flux<UserAuthorityModel> boundUsers(String authorityId) {
        return authorityRepository.findUsersByAuthorityId(authorityId);
    }

    public Flux<User> unboundUsers(String authorityId) {
        return authorityRepository.findUsersWithoutAuthorityId(authorityId);
    }

    public Flux<GroupAuthorityModel> boundGroups(String authorityId) {
        return authorityRepository.findGroupsByAuthorityId(authorityId);
    }

    public Flux<Group> unboundGroups(String authorityId) {
        return authorityRepository.findGroupsWithoutAuthorityId(authorityId);
    }

    public Flux<RoleAuthorityModel> boundRoles(String authorityId) {
        return authorityRepository.findRolesByAuthorityId(authorityId);
    }

    public Flux<Role> unboundRoles(String authorityId) {
        return authorityRepository.findRolesWithoutAuthorityId(authorityId);
    }

}
