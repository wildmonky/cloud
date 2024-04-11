package org.lizhao.cloud.gateway.handler;

import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.exception.CustomException;
import org.lizhao.cloud.gateway.repository.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Description 用户通用操作
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-06 22:12
 * @since 0.0.1-SNAPSHOT
 */
public class UserHandler {

    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final AuthorityRepository authorityRepository;

    private final GroupUserRelationRepository groupUserRelationRepository;

    public UserHandler(RoleRepository roleRepository,
                        GroupRepository groupRepository,
                        AuthorityRepository authorityRepository,
                        GroupUserRelationRepository groupUserRelationRepository
    ) {
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.authorityRepository = authorityRepository;
        this.groupUserRelationRepository = groupUserRelationRepository;
    }


    /**
     * 查询用户绑定的所有权限
     * @param user 用户
     * @return 用户绑定所有的权限
     */
    public Flux<Authority> userAllAuthorities(User user) {
        if (Objects.isNull(user) || user.getId() == null) {
            throw new CustomException("用户为空");
        }

        //1. 获取用户所在组（包含子组）
        Flux<Group> allUserGroupFlux = userGroupsIncludeChild(user.getId());

        // 获取组绑定的角色
        Flux<Role> roleDirectFromGroupFlux = allUserGroupFlux.hasElements()
                .flatMapMany(hasElement -> hasElement ? groupRoles(allUserGroupFlux) : Flux.empty());

        // 获取用户绑定的角色
        Flux<Role> roleDirectFromUserFlux = userRoles(user.getId());

        Flux<Role> allRoleFlux = roleDirectFromGroupFlux.concatWith(roleDirectFromUserFlux);
        //2. 获取所有角色对应的子角色（包含父角色）
        Flux<Role> allUserRoleFlux = allRoleFlux.hasElements()
                .flatMapMany(hasElements -> hasElements ? treeRole(allRoleFlux) : Flux.empty());

        //3. 用户直接绑定的权限
        Flux<Authority> authoritiesDirectFromUser = authorityRepository.findAuthoritiesByUsers(Mono.just(user.getId()));
        //4. 获取用户绑定的所有角色（组+子组+用户）的权限 （包含子角色）
        Flux<Authority> authoritiesFromRoleWithinUserFlux = allUserRoleFlux.hasElements()
                .flatMapMany(hasElements -> hasElements ? roleAuthorities(allUserRoleFlux) : Flux.empty());
        //5. 获取用户绑定的组（组+子组）的权限
        Flux<Authority> authoritiesDirectFromGroupWithinUserFlux = allUserGroupFlux.hasElements()
                .flatMapMany(hasElements -> hasElements ? groupAuthorities(allUserGroupFlux) : Flux.empty());

        return authoritiesDirectFromUser.concatWith(authoritiesFromRoleWithinUserFlux)
                .concatWith(authoritiesDirectFromGroupWithinUserFlux)
                .concatWith(allUserRoleFlux.hasElements().flatMapMany(hasElements ->
                        allUserRoleFlux.map(role -> {
                        Authority authority = new Authority();
                        authority.setName("ROLE_" + role.getName());
                        return authority;
                    })
                ));
    }

    /**
     * 获取用户所属组（包含所属组的子组）
     * @param userId 用户id
     * @return 用户所属组（包含所属组的子组）
     */
    public Flux<Group> userGroupsIncludeChild(String userId) {
        return groupRepository.findGroupsIncludeChildByUserId(userId);
    }


    /**
     * 获取用户所属组（不包含所属组的子组）
     * @param userId 用户id
     * @return 用户所属组（不包含所属组的子组）
     */
    public Flux<Group> userGroups(String userId) {
        return groupRepository.findGroupsByUserId(userId);
    }

    /**
     * 查询用户直接绑定的角色(不包含子角色)
     * @param userId 用户id
     * @return 用户直接绑定的角色(不包含子角色)
     */
    public Flux<Role> userRoles(String userId) {
        return roleRepository.findRolesByUserId(userId);
    }

    /**
     * 查询角色绑定权限(不包含子角色)
     * @param roleFlux 角色
     * @return 角色绑定权限
     */
    public Flux<Authority> roleAuthorities(Flux<Role> roleFlux) {
        return authorityRepository.findAuthoritiesByRoles(roleFlux.map(Role::getId));
    }

    /**
     * 查询组直接绑定的权限(不包含子组)
     * @param groupFlux 用户组
     * @return 组直接绑定的权限
     */
    public Flux<Authority> groupAuthorities(Flux<Group> groupFlux) {
        return authorityRepository.findAuthoritiesByGroups(groupFlux.map(Group::getId));
    }

    /**
     * 获取组直接绑定的角色
     * @param groupFlux 组id
     * @return userGroups(userId)
     */
    public Flux<Role> groupRoles(Flux<Group> groupFlux) {
        return  roleRepository.rolesInGroup(groupFlux.map(Group::getId));
    }

    /**
     * 获取根组下的所有子组（包含根组）
     *
     * @param rootGroup 根组
     * @return 根组以下所有的子组
     */
    public Flux<Group> treeGroup(Flux<Group> rootGroup) {
        return groupRepository.child(rootGroup.map(Group::getId));
    }

    /**
     * 获取根角色下的所有子组（包含根角色）
     *
     * @param rootRole 根角色
     * @return 根角色以下所有的子角色
     */
    public Flux<Role> treeRole(Flux<Role> rootRole) {
        return roleRepository.child(rootRole.map(Role::getId));
    }

    /**
     * 将用户绑定到组
     * @return 用户已绑定的组
     */
    public Flux<GroupUserRelation> bindToGroups(Map<User, Collection<Group>> userGroupMap) {
        Publisher<GroupUserRelation> bound = CommonHandler.bind(userGroupMap,
                (user, group) -> {
                    if (user.getId() == null || group.getId() == null) {
                        return null;
                    }
                    GroupUserRelation relation = new GroupUserRelation();
                    relation.setGroupId(group.getId());
                    relation.setUserId(user.getId());
                    return relation;
                }, p -> groupUserRelationRepository.saveAll(Flux.from(p)));
        return Flux.from(bound);
    }

    /**
     * 将用户绑定到组
     * @return 用户已绑定的组
     */
    public Flux<GroupUserRelation> bindMoreToGroup(Map<Group, Collection<User>> userGroupMap) {
        Publisher<GroupUserRelation> bound = CommonHandler.bind(userGroupMap,
                (group, user) -> {
                    if (user.getId() == null || group.getId() == null) {
                        return null;
                    }
                    GroupUserRelation relation = new GroupUserRelation();
                    relation.setGroupId(group.getId());
                    relation.setUserId(user.getId());
                    return relation;
                }, groupUserRelationRepository::saveAll);
        return Flux.from(bound);
    }

    /**
     * 将用户与组解绑
     * @param relation 用户与组绑定关系
     */
    public Mono<Void> unbindFromGroup(GroupUserRelation relation) {

        if (relation == null) {
            return Mono.error(new Throwable("空的绑定关系"));
        }
        if (relation.getUserId() == null) {
            return Mono.error(new Throwable("用户id为空"));
        }
        if (relation.getGroupId() == null) {
            return Mono.error(new Throwable("用户组id为空"));
        }

        return groupUserRelationRepository.findByGroupIdAndUserId(relation.getGroupId(), relation.getUserId())
                .switchIfEmpty(Mono.error(new Throwable("不存在用户与组的绑定关系")))
                .collectList()
                .flatMap(l -> {
                    if (l.size() > 1) {
                        return Mono.error(new Throwable("存在重复的绑定的关系"));
                    }
                    return groupUserRelationRepository.deleteById(l.get(0).getId());
                });
    }
}
