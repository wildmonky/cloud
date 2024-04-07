package org.lizhao.cloud.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

/**
 * Description 角色 Controller
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:10
 * @since 0.0.1-SNAPSHOT
 */
@RequestMapping("/gateway/role")
@Controller
public class RoleController {

    @PostMapping("/bind/to/user")
    public Mono<Boolean> bindRoleToUser() {
        return Mono.empty();
    }

    @PostMapping("/bind/to/group")
    public Mono<Boolean> bindRoleToGroup() {
        return Mono.empty();
    }

}
