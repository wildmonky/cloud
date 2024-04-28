package org.lizhao.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.UserInfo;
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

@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "所有用户")
    @GetMapping("/all")
    public Flux<User> searchAll() {
        return userService.searchAll();
    }

    @Operation(summary = "根据用户名查询")
    @GetMapping("/{username}")
    public Mono<User> search(@PathVariable("username")String username) {
        return userService.searchByName(username);
    }

    @Operation(summary = "根据用户id查询")
    @GetMapping("/details/{username}")
    public Mono<UserInfo> searchDetailsByName(@PathVariable("username") String name) {
        return userService.searchDetailsByName(name);
    }

    @Operation(summary = "根据用户id查询")
    @GetMapping("/")
    public Mono<UserInfo> searchDetails(@RequestParam("id") String id) {
        return userService.searchDetailsById(id);
    }

    @Operation(summary = "保存用户信息")
    @PostMapping("/")
    public Mono<Boolean> save(@RequestBody User user) {
        return userService.save(user).hasElement();
    }

    @Operation(summary = "移除指定的用户信息")
    @DeleteMapping("/")
    public Mono<Void> remove(@RequestBody User user) {
        return userService.remove(user);
    }

}
