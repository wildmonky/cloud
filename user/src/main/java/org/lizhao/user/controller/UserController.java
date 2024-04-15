package org.lizhao.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.user.service.RelationService;
import org.lizhao.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Description 用户 Controller
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-01 0:17
 * @since 0.0.1-SNAPSHOT
 */

@RequestMapping("/")
@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RelationService relationService;

    @Operation(summary = "所有用户")
    @GetMapping("/searchAll")
    public Flux<User> searchAll() {
        return userService.searchAll();
    }

    @Operation(summary = "根据用户名查询")
    @GetMapping("/search/{username}")
    public Mono<User> search(@PathVariable("username")String username) {
        return userService.searchByName(username);
    }

    @Operation(summary = "保存用户信息")
    @PostMapping("/save")
    public Mono<Boolean> save(@RequestBody User user) {
        return userService.save(user).hasElement();
    }

    @Operation(summary = "移除指定的用户信息")
    @PostMapping("/remove")
    public Mono<Void> remove(@RequestBody User user) {
        return userService.remove(user);
    }

    @Operation(summary = "将用户绑定到组")
    @PostMapping("/bind/to/group")
    public Mono<Boolean> bindGroupToUser(@RequestBody Map<Group, Collection<User>> map) {
        return userService.bindUsersToGroup(map)
                .collectList()
                .map(l -> l.size() == map.values().stream().map(Collection::size).count());
    }

    @Operation(summary = "将用户与组解绑")
    @PostMapping("/unbind/from/group")
    public Mono<Void> unbindFromGroup(@RequestBody GroupUserRelation relation) {
        return userService.unbindFromGroup(relation);
    }

    @Operation(summary = "开关用户与组关系")
    @GetMapping("/group/relation/turn")
    public Mono<Boolean> groupRelationChange(String relationId) {
        return relationService.turnRelationBetweenUserAndGroup(relationId);
    }

}
