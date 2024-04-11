package org.lizhao.cloud.gateway.controller;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.serviceImpl.RoleServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 角色 Controller
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:10
 * @since 0.0.1-SNAPSHOT
 */
@RequestMapping("/role")
@Controller
public class RoleController {

    @Resource
    private RoleServiceImpl roleService;

    @GetMapping("/searchAll")
    public Flux<Role> searchAll() {
        return roleService.searchAll();
    }

    @GetMapping("/searchBoundUsers")
    public Flux<User> searchBoundUsers(String roleId) {
        return roleService.searchBoundUsers(roleId);
    }

    @GetMapping("/searchBoundGroups")
    public Flux<Group> searchBoundGroups(String roleId) {
        return roleService.searchBoundGroups(roleId);
    }

    @PostMapping("/bind/to/user")
    public Mono<Boolean> bindRoleToUser(Map<Role, Collection<User>> map) {
        return roleService.bindRoleToUsers(map);
    }

    @PostMapping("/unbind/from/user")
    public Mono<Void> unbindRoleFromUser(String userRoleRelationId) {
        return roleService.unbindRoleFromUser(userRoleRelationId);
    }

    @PostMapping("/bind/to/group")
    public Mono<Boolean> bindRoleToGroup(Map<Role, Collection<Group>> map) {
        return roleService.bindRoleToGroups(map);
    }

    @PostMapping("/unbind/from/group")
    public Mono<Void> unbindRoleFromGroup(String groupRoleRelationId) {
        return roleService.unbindRoleFromGroup(groupRoleRelationId);
    }

}
