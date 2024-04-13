package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.TreeNode;
import org.lizhao.cloud.gateway.handler.RoleHandler;
import org.lizhao.cloud.gateway.model.GroupRoleModel;
import org.lizhao.cloud.gateway.model.UserRoleModel;
import org.lizhao.cloud.gateway.repository.RoleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description 角色服务实现类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-09 17:02
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class RoleServiceImpl {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private RoleHandler roleHandler;

    public Flux<Role> searchAll() {
        return roleRepository.findAll();
    }

    public Mono<List<Role>> searchTree() {
        return roleRepository.findAll().collectList().map(l ->
            roleHandler.generateTree(l)
        );
    }

    public Flux<UserRoleModel> searchBoundUsers(String roleId) {
        return roleRepository.findUsersByRoleId(roleId);
    }

    public Flux<User> searchUnboundUsers(String roleId) {
        return roleRepository.findUsersWithoutRoleId(roleId);
    }

    public Flux<GroupRoleModel> searchBoundGroups(String roleId) {
        return roleRepository.findGroupsByRoleId(roleId);
    }

    public Flux<Group> searchUnboundGroups(String roleId) {
        return roleRepository.findGroupsWithoutRoleId(roleId);
    }

    public Mono<Role> save(Role role) {
        return roleRepository.save(role);
    }

    public Mono<Void> remove(String roleId) {
        return roleRepository.deleteById(roleId);
    }

    public Flux<UserRoleRelation> bindRolesToUser(Map<User, Collection<Role>> map) {
        return roleHandler.bindMoreToUser(map);
    }

    public Flux<UserRoleRelation> bindRoleToUsers(Map<Role, Collection<User>> map) {
        return roleHandler.bindToUsers(map);
    }

    public Mono<Void> unbindRoleFromUser(UserRoleRelation relation) {
        return roleHandler.unbindFromUser(relation);
    }

    public Flux<GroupRoleRelation> bindRolesToGroup(Map<Group, Collection<Role>> map) {
        return roleHandler.bindMoreToGroup(map);
    }

    public Flux<GroupRoleRelation> bindRoleToGroups(Map<Role, Collection<Group>> map) {
        return roleHandler.bindToGroups(map);
    }

    public Mono<Void> unbindRoleFromGroup(GroupRoleRelation relation) {
        return roleHandler.unbindFromGroup(relation);
    }
}
