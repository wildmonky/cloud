package org.lizhao.user.service;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.exception.MessageException;
import org.lizhao.user.handler.RoleHandler;
import org.lizhao.user.model.GroupRoleModel;
import org.lizhao.user.model.RoleModel;
import org.lizhao.user.model.UserRoleModel;
import org.lizhao.user.repository.GroupRoleRelationRepository;
import org.lizhao.user.repository.RoleRepository;
import org.lizhao.user.repository.UserRoleRelationRepository;
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
public class RoleService {

    @Resource
    private RoleRepository roleRepository;
    @Resource
    private GroupRoleRelationRepository groupRoleRelationRepository;
    @Resource
    private UserRoleRelationRepository userRoleRelationRepository;

    @Resource
    private RoleHandler roleHandler;

    public Flux<Role> searchAll() {
        return roleRepository.findAll();
    }

    public Mono<List<RoleModel>> searchTree() {
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

    public Mono<Void> remove(Role role) {
        if (role == null) {
            return  Mono.error(new MessageException("角色为空"));
        }
        if (role.getId() == null) {
            return  Mono.error(new MessageException("角色Id为空"));
        }
        return roleRepository.existsAllByParentId(role.getId())
                .doOnSuccess(flag -> {
                    if (flag) {
                        throw new MessageException("{}角色包含子级角色，无法直接删除", role.getName());
                    }
                }).flatMap(flag ->
                    userRoleRelationRepository.existsAllByRoleId(role.getId())
                            .doOnSuccess(childFlag -> {
                                if (childFlag) {
                                    throw new MessageException("{}角色绑定了用户，无法直接删除", role.getName());
                                }
                            })
                ).flatMap(flag ->
                    groupRoleRelationRepository.existsAllByRoleId(role.getId())
                            .flatMap(childFlag -> {
                                if (childFlag) {
                                    return Mono.error(new MessageException("{}角色绑定了组，无法直接删除", role.getName()));
                                }
                                return roleRepository.deleteById(role.getId());
                            })
                );
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
