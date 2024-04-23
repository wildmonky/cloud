package org.lizhao.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.user.model.UserGroupModel;
import org.lizhao.user.service.GroupService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    private GroupService groupService;

    @PostMapping("")
    public Mono<Group> save(@RequestBody Group group) {
        return groupService.save(group);
    }

    @DeleteMapping("")
    public Mono<Void> remove(@RequestBody Group group) {
        return groupService.remove(group);
    }

    @Operation(summary = "所有用户组")
    @GetMapping("/all")
    public Flux<Group> searchAll() {
        return groupService.searchAll();
    }

    @Operation(summary = "所有用户组")
    @GetMapping("/tree")
    public Mono<List<Group>> searchTree() {
        return groupService.searchTree();
    }

    @Operation(summary = "组中所有绑定的用户")
    @GetMapping("/user/boundUsers")
    public Flux<UserGroupModel> searchBoundUsers(String groupId) {
        return groupService.searchBoundUsers(groupId);
    }

    @Operation(summary = "未绑定到组中的用户")
    @GetMapping("/user/unboundUsers")
    public Flux<User> searchUnboundUsers(String groupId) {
        return groupService.searchUnboundUsers(groupId);
    }

    @Operation(summary = "将用户绑定到组")
    @PostMapping("/user")
    public Mono<Boolean> bindGroupToUser(@RequestBody Map<Group, Collection<User>> map) {
        return groupService.bindUsersToGroup(map)
                .collectList()
                .map(l -> l.size() == map.values().stream().map(Collection::size).count());
    }

    @Operation(summary = "将用户与组解绑")
    @DeleteMapping("/user")
    public Mono<Void> unbindFromGroup(@RequestBody GroupUserRelation relation) {
        return groupService.unbindFromGroup(relation);
    }

}
