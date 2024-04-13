package org.lizhao.cloud.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.TreeNode;
import org.lizhao.cloud.gateway.model.UserGroupModel;
import org.lizhao.cloud.gateway.serviceImpl.GroupServiceImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description 组 Controller
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-10 12:27
 * @since 0.0.1-SNAPSHOT
 */
@RequestMapping("/group")
@RestController
public class GroupController {

    @Resource
    private GroupServiceImpl groupService;

    @Operation(summary = "所有用户组")
    @GetMapping("/searchAll")
    public Flux<Group> searchAll() {
        return groupService.searchAll();
    }

    @Operation(summary = "所有用户组")
    @GetMapping("/searchTree")
    public Mono<List<Group>> searchTree() {
        return groupService.searchTree();
    }

    @Operation(summary = "组中所有绑定的用户")
    @GetMapping("/searchBoundUsers")
    public Flux<UserGroupModel> searchBoundUsers(String groupId) {
        return groupService.searchBoundUsers(groupId);
    }

    @Operation(summary = "未绑定到组中的用户")
    @GetMapping("/searchUnboundUsers")
    public Flux<User> searchUnboundUsers(String groupId) {
        return groupService.searchUnboundUsers(groupId);
    }

    @PostMapping("/save")
    public Mono<Group> save(@RequestBody Group group) {
        return groupService.save(group);
    }

    @PostMapping("/remove")
    public Mono<Void> remove(@RequestBody Group group) {
        return groupService.remove(group);
    }

}
