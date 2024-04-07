package org.lizhao.cloud.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.serviceImpl.AuthorityServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Controller
@RequestMapping("/gateway/authority")
public class AuthorityController {

    @Resource
    private AuthorityServiceImpl authorityService;

    @Operation(summary = "保存权限信息")
    @PostMapping("/save")
    public Mono<Authority> save(@RequestBody Authority authority) {
        return authorityService.save(authority);
    }

    @Operation(summary = "移除权限信息")
    @PostMapping("/remove")
    public Mono<Void> remove(@RequestBody Authority authority) {
        return authorityService.remove(authority);
    }

    @Operation(summary = "将权限绑定给用户")
    @PostMapping("/bind/to/user")
    public Mono<Boolean> bindAuthorityToUser(Map<User, Collection<Authority>> userAuthorityMap) {
        return authorityService.bindToUser(userAuthorityMap);
    }

    @Operation(summary = "将权限绑定给角色")
    @PostMapping("/bind/to/role")
    public Mono<Boolean> bindAuthorityToRole(Map<Role, Collection<Authority>> roleAuthorityMap) {
        return authorityService.bindToRole(roleAuthorityMap);
    }

    @Operation(summary = "将权限绑定给组")
    @PostMapping("/bind/to/group")
    public Mono<Boolean> bindAuthorityToGroup(Map<Group, Collection<Authority>> groupAuthorityMap) {
        return authorityService.bindToGroup(groupAuthorityMap);
    }

}
