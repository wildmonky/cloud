package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupAuthorityRelation;
import org.lizhao.base.entity.relation.RoleAuthorityRelation;
import org.lizhao.base.entity.relation.UserAuthorityRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.handler.AuthorityHandler;
import org.lizhao.cloud.gateway.model.GroupAuthorityModel;
import org.lizhao.cloud.gateway.model.RoleAuthorityModel;
import org.lizhao.cloud.gateway.model.UserAuthorityModel;
import org.lizhao.cloud.gateway.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 权限服务实现类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:14
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class AuthorityServiceImpl {

    @Resource
    private AuthorityRepository authorityRepository;

    @Resource
    private AuthorityHandler authorityHandler;

    public Flux<Authority> search() {
        return authorityRepository.findAll();
    }

    /**
     * 查询直接绑定权限的用户
     * @param authorityId 权限id
     * @return 绑定权限的用户
     */
    public Flux<UserAuthorityModel> searchBoundUser(String authorityId) {
        return authorityHandler.boundUsers(authorityId);
    }

    /**
     * 查询未绑定权限的用户
     * @param authorityId 权限id
     * @return 未绑定权限的用户
     */
    public Flux<User> searchUnboundUser(String authorityId) {
        return authorityHandler.unboundUsers(authorityId);
    }

    /**
     * 查询直接绑定权限的组
     * @param authorityId 权限id
     * @return 组直接绑定的权限
     */
    public Flux<GroupAuthorityModel> searchBoundGroup(String authorityId) {
        return authorityHandler.boundGroups(authorityId);
    }

    /**
     * 查询未绑定该权限的组
     * @param authorityId 权限id
     * @return 未直接绑定该权限的组
     */
    public Flux<Group> searchUnboundGroup(String authorityId) {
        return authorityHandler.unboundGroups(authorityId);
    }


    /**
     * 查询直接绑定权限的角色
     * @param authorityId 权限id
     * @return 角色直接绑定的权限
     */
    public Flux<RoleAuthorityModel> searchBoundRole(String authorityId) {
        return authorityHandler.boundRoles(authorityId);
    }

    /**
     * 查询w未直接绑定权限的角色
     * @param authorityId 权限id
     * @return 未直接绑定的权限的角色
     */
    public Flux<Role> searchUnboundRole(String authorityId) {
        return authorityHandler.unboundRoles(authorityId);
    }

    public Mono<Authority> save(Authority authority) {
        return authorityRepository.save(authority);
    }

    public Mono<Void> remove(Authority authority) {
        return authorityRepository.delete(authority);
    }

    public Flux<UserAuthorityRelation> bindToUser(Map<Authority, Collection<User>> userAuthorityMap) {
        return authorityHandler.bindToUsers(userAuthorityMap);
    }

    public Flux<GroupAuthorityRelation> bindToGroup(Map<Authority, Collection<Group>> groupAuthorityMap) {
        return authorityHandler.bindToGroups(groupAuthorityMap);
    }

    public Flux<RoleAuthorityRelation> bindToRole(Map<Authority, Collection<Role>> roleAuthorityMap) {
        return authorityHandler.bindToRoles(roleAuthorityMap);
    }

    public Mono<Void> unbindFromGroup(GroupAuthorityRelation relation) {
        return authorityHandler.unbindFromGroup(relation);
    }

    public Mono<Void> unbindFromUser(UserAuthorityRelation relation) {
        return authorityHandler.unbindFromUser(relation);
    }

    public Mono<Void> unbindFromRole(RoleAuthorityRelation relation) {
        return authorityHandler.unbindFromRole(relation);
    }

}
