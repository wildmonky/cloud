package org.lizhao.user.controller;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.user.model.GroupRoleModel;
import org.lizhao.user.model.RoleModel;
import org.lizhao.user.model.UserRoleModel;
import org.lizhao.user.service.RoleService;
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
    private RoleService roleService;

    @GetMapping("/all")
    public Flux<Role> searchAll() {
        return roleService.searchAll();
    }

    @GetMapping("/tree")
    public Mono<List<RoleModel>> searchTree() {
        return roleService.searchTree();
    }

    @GetMapping("/user/boundUsers")
    public Flux<UserRoleModel> searchBoundUsers(String roleId) {
        return roleService.searchBoundUsers(roleId);
    }

    @GetMapping("/user/unboundUsers")
    public Flux<User> searchUnboundUsers(String roleId) {
        return roleService.searchUnboundUsers(roleId);
    }

    @GetMapping("/group/boundGroups")
    public Flux<GroupRoleModel> searchBoundGroups(String roleId) {
        return roleService.searchBoundGroups(roleId);
    }

    @GetMapping("/group/unboundGroups")
    public Flux<Group> searchUnboundGroups(String roleId) {
        return roleService.searchUnboundGroups(roleId);
    }

    @PostMapping("")
    public Mono<Boolean> save(@RequestBody Role role) {
        return roleService.save(role).hasElement();
    }

    @DeleteMapping("")
    public Mono<Void> remove(@RequestBody Role role) {
        return roleService.remove(role);
    }

    @PostMapping("/user")
    public Mono<Boolean> bindRoleToUser(@RequestBody Map<Role, Collection<User>> map) {
        return roleService.bindRoleToUsers(map)
                .collectList()
                .map(l -> l.size() == map.values().stream().map(Collection::size).count());
    }

    @DeleteMapping("/user")
    public Mono<Void> unbindRoleFromUser(@RequestBody UserRoleRelation relation) {
        return roleService.unbindRoleFromUser(relation);
    }

    @PostMapping("/group")
    public Mono<Boolean> bindRoleToGroup(@RequestBody Map<Role, Collection<Group>> map) {
        return roleService.bindRoleToGroups(map)
                .collectList()
                .map(l -> l.size() == map.values().stream().map(Collection::size).count());
    }

    @DeleteMapping("/group")
    public Mono<Void> unbindRoleFromGroup(@RequestBody GroupRoleRelation relation) {
        return roleService.unbindRoleFromGroup(relation);
    }

}
