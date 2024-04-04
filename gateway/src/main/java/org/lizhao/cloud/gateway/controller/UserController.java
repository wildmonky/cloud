package org.lizhao.cloud.gateway.controller;

import jakarta.annotation.Resource;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.lizhao.cloud.gateway.serviceImpl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Description TODO 用户服务
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
    private UserServiceImpl userService;

    @PostMapping("/online")
    public Flux<GatewayUser> online(String token) {
        return userService.onlineUser(token);
    }

    @PostMapping("/offline")
    public Mono<Boolean> offline(@RequestBody String token) {
        return userService.offline(token);
    }

}
