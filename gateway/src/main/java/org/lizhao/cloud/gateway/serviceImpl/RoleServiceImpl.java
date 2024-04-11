package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.handler.RoleHandler;
import org.lizhao.cloud.gateway.repository.RoleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
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

    public Flux<User> searchBoundUsers(String roleId) {
        return roleRepository.findUsersByRoleId(roleId, true);
    }

    public Flux<Group> searchBoundGroups(String roleId) {
        return roleRepository.findGroupsByRoleId(roleId, true);
    }

    public Mono<Boolean> bindRolesToUser(Map<User, Collection<Role>> map) {
        return roleHandler.bindMoreToUser(map).hasElements();
    }

    public Mono<Boolean> bindRoleToUsers(Map<Role, Collection<User>> map) {
        return roleHandler.bindToUsers(map).hasElements();
    }

    public Mono<Void> unbindRoleFromUser(String relationId) {
        return roleHandler.unbindFromUser(relationId);
    }

    public Mono<Boolean> bindRolesToGroup(Map<Group, Collection<Role>> map) {
        return roleHandler.bindMoreToGroup(map).hasElements();
    }

    public Mono<Boolean> bindRoleToGroups(Map<Role, Collection<Group>> map) {
        return roleHandler.bindToGroups(map).hasElements();
    }

    public Mono<Void> unbindRoleFromGroup(String relationId) {
        return roleHandler.unbindFromGroup(relationId);
    }
}
