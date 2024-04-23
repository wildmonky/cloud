package org.lizhao.user.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.lizhao.user.service.AuthorityService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 权限 Controller
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-24 21:04
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @Resource
    private AuthorityService authorityService;

    @Operation(summary = "查询所有权限信息")
    @GetMapping("/all")
    public Flux<Authority> searchAll() {
        return authorityService.search();
    }

    @Operation(summary = "查询绑定该权限的用户信息")
    @GetMapping("/user/boundUsers")
    public Flux<UserAuthorityModel> searchBoundUser(String authorityId) {
        return authorityService.searchBoundUser(authorityId);
    }

    @Operation(summary = "查询未绑定该权限的用户信息")
    @GetMapping("/user/unboundUsers")
    public Flux<User> searchUnboundUser(String authorityId) {
        return authorityService.searchUnboundUser(authorityId);
    }

    @Operation(summary = "查询组权限信息")
    @GetMapping("/group/boundGroups")
    public Flux<GroupAuthorityModel> searchBoundGroup(String authorityId) {
        return authorityService.searchBoundGroup(authorityId);
    }

    @Operation(summary = "查询未绑定该权限的用户信息")
    @GetMapping("/group/unboundGroups")
    public Flux<Group> searchUnboundGroup(String authorityId) {
        return authorityService.searchUnboundGroup(authorityId);
    }

    @Operation(summary = "查询角色权限信息")
    @GetMapping("/role/boundRoles")
    public Flux<RoleAuthorityModel> searchBoundRole(String authorityId) {
        return authorityService.searchBoundRole(authorityId);
    }

    @Operation(summary = "查询未绑定权限的角色信息")
    @GetMapping("/role/unboundRoles")
    public Flux<Role> searchUnboundRole(String authorityId) {
        return authorityService.searchUnboundRole(authorityId);
    }

    @Operation(summary = "保存权限信息")
    @PostMapping("")
    public Mono<Authority> save(@RequestBody Authority authority) {
        return authorityService.save(authority);
    }

    @Operation(summary = "移除权限信息")
    @DeleteMapping("")
    public Mono<Void> remove(@RequestBody Authority authority) {
        return authorityService.remove(authority);
    }

    @Operation(summary = "将权限绑定给用户")
    @PostMapping("/user")
    public Mono<Boolean> bindAuthorityToUser(@RequestBody Map<Authority, Collection<User>> userAuthorityMap) {
        return authorityService.bindToUser(userAuthorityMap)
                .collectList()
                .map(l -> l.size() == userAuthorityMap.values().stream().map(Collection::size).count());
    }

    @Operation(summary = "将权限绑定给角色")
    @PostMapping("/role")
    public Mono<Boolean> bindAuthorityToRole(@RequestBody Map<Authority, Collection<Role>> roleAuthorityMap) {
        return authorityService.bindToRole(roleAuthorityMap)
                .collectList()
                .map(l -> l.size() == roleAuthorityMap.values().stream().map(Collection::size).count());
    }

    @Operation(summary = "将权限绑定给组")
    @PostMapping("/group")
    public Mono<Boolean> bindAuthorityToGroup(@RequestBody Map<Authority, Collection<Group>> groupAuthorityMap) {
        return authorityService.bindToGroup(groupAuthorityMap)
                .collectList()
                .map(l -> l.size() == groupAuthorityMap.values().stream().map(Collection::size).count());
    }

    @Operation(summary = "将权限与组解绑")
    @DeleteMapping("/group")
    public Mono<Void> removeAuthorityFromGroup(@RequestBody GroupAuthorityRelation relation) {
        return authorityService.unbindFromGroup(relation);
    }

    @Operation(summary = "将权限与用户解绑")
    @DeleteMapping("/user")
    public Mono<Void> removeAuthorityFromUser(@RequestBody UserAuthorityRelation relation) {
        return authorityService.unbindFromUser(relation);
    }

    @Operation(summary = "将权限与角色解绑")
    @DeleteMapping("/role")
    public Mono<Void> removeAuthorityFromRole(@RequestBody RoleAuthorityRelation relation) {
        return authorityService.unbindFromRole(relation);
    }

}
