package org.lizhao.cloud.gateway.controller;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.model.GroupRoleModel;
import org.lizhao.cloud.gateway.model.UserRoleModel;
import org.lizhao.cloud.gateway.serviceImpl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
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
@RestController
public class RoleController {

    @Resource
    private RoleServiceImpl roleService;

    @GetMapping("/searchAll")
    public Flux<Role> searchAll() {
        return roleService.searchAll();
    }

    @GetMapping("/searchTree")
    public Mono<List<Role>> searchTree() {
        return roleService.searchTree();
    }

    @GetMapping("/searchBoundUsers")
    public Flux<UserRoleModel> searchBoundUsers(String roleId) {
        return roleService.searchBoundUsers(roleId);
    }

    @GetMapping("/searchUnboundUsers")
    public Flux<User> searchUnboundUsers(String roleId) {
        return roleService.searchUnboundUsers(roleId);
    }

    @GetMapping("/searchBoundGroups")
    public Flux<GroupRoleModel> searchBoundGroups(String roleId) {
        return roleService.searchBoundGroups(roleId);
    }

    @GetMapping("/searchUnboundGroups")
    public Flux<Group> searchUnboundGroups(String roleId) {
        return roleService.searchUnboundGroups(roleId);
    }

    @PostMapping("/save")
    public Mono<Boolean> save(@RequestBody Role role) {
        return roleService.save(role).hasElement();
    }

    @GetMapping("/remove")
    public Mono<Void> remove(String roleId) {
        return roleService.remove(roleId);
    }

    @PostMapping("/bind/to/user")
    public Mono<Boolean> bindRoleToUser(@RequestBody Map<Role, Collection<User>> map) {
        return roleService.bindRoleToUsers(map)
                .collectList()
                .map(l -> l.size() == map.values().stream().map(Collection::size).count());
    }

    @PostMapping("/unbind/from/user")
    public Mono<Void> unbindRoleFromUser(@RequestBody UserRoleRelation relation) {
        return roleService.unbindRoleFromUser(relation);
    }

    @PostMapping("/bind/to/group")
    public Mono<Boolean> bindRoleToGroup(@RequestBody Map<Role, Collection<Group>> map) {
        return roleService.bindRoleToGroups(map)
                .collectList()
                .map(l -> l.size() == map.values().stream().map(Collection::size).count());
    }

    @PostMapping("/unbind/from/group")
    public Mono<Void> unbindRoleFromGroup(@RequestBody GroupRoleRelation relation) {
        return roleService.unbindRoleFromGroup(relation);
    }

}
