package org.lizhao.cloud.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.serviceImpl.GroupServiceImpl;
import org.lizhao.cloud.gateway.serviceImpl.UserServiceImpl;
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

@RequestMapping("/gateway/user")
@RestController
public class UserGroupController {

    @Resource
    private UserServiceImpl userService;

    @Operation(summary = "在线用户列表")
    @PostMapping("/online")
    public Flux<GatewayUser> online(String token) {
        return userService.onlineUser(token);
    }

    @Operation(summary = "下线指定的在线用户")
    @PostMapping("/offline")
    public Mono<Boolean> offline(@RequestBody String token) {
        return userService.offline(token);
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
    public Mono<Boolean> bindGroupToUser(@RequestBody Map<User, Collection<Group>> map) {
        return userService.bindUserToGroup(map).hasElements();
    }


    @RequestMapping("/group")
    public static class GroupController {

        @Resource
        private GroupServiceImpl groupService;

        @PostMapping("/save")
        public Mono<Group> save(Group group) {
            return groupService.save(group);
        }

        @PostMapping("/remove")
        public Mono<Void> remove(Group group) {
            return groupService.remove(group);
        }

    }
}
