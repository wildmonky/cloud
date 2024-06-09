package org.lizhao.realtime.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.entity.realtime.RoomMember;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.realtime.RoomModel;
import org.lizhao.realtime.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Description room controller
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-07 23:08
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@RequestMapping("/room")
@RestController
public class RoomController {

    @Resource
    private RoomService roomService;

    /**
     * 创建 新的房间
     * @param room 房间
     */
    @PutMapping("")
    public RoomModel create(@RequestBody Room room) {
        return roomService.create(room);
    }

    /**
     * 重设房主
     * @param roomName 房间名
     * @param userInfo 房主信息
     */
    @PatchMapping("/{roomName}/owner")
    public void resetOwner(@PathVariable("roomName")String roomName, @RequestParam SimpleUserInfo userInfo) {
        roomService.resetOwner(roomName, userInfo);
    }

    /**
     * 重设房间密码
     * @param roomName 房间名
     * @param secret 房间密码
     */
    @PatchMapping("/{roomName}/{secret}")
    public void resetSecret(@PathVariable("roomName")String roomName, @PathVariable("secret")String secret) {
        roomService.resetSecretWithinRoom(roomName, secret);
    }

    /**
     * 删除房间 并 清空所有房间成员
     * @param roomName 房间名
     */
    @DeleteMapping("/{roomName}")
    public void removeRoom(@PathVariable("roomName")String roomName) {
        roomService.remove(roomName);
    }

    /**
     * 房间成员邀请 用户
     * @param roomName 房间名
     * @param userIds 被邀请的用户id列表
     */
    @PutMapping("/{roomName}/member")
    public void invite(@PathVariable("roomName")String roomName, @RequestBody Set<String> userIds) {
        roomService.inviteMembers(roomName, userIds);
    }

    /**
     * 接收房间邀请
     * @param roomName 房间名
     */
    @PutMapping("/{roomName}/invite/{inviteId}")
    public RoomMember acceptInvite(@PathVariable("roomName") String roomName, @PathVariable("inviteId") String inviteId) {
        return roomService.acceptInvite(roomName, inviteId);
    }

    /**
     * 拒绝房间邀请
     * @param roomName 房间名
     */
    @DeleteMapping("/{roomName}/invite/{inviteId}")
    public void refuseInvite(@PathVariable("roomName") String roomName, @PathVariable("inviteId") String inviteId) {
        roomService.refuseInvite(roomName, inviteId);
    }

    /**
     * 用户自己尝试加入房间
     * @param roomName 房间名
     * @param secret 房间密码
     */
    @PutMapping("/user/{roomName}/{secret}")
    public RoomMember join(@PathVariable("roomName")String roomName, @PathVariable(value = "secret", required = false)String secret) {
        return roomService.joinRoom(roomName, secret);
    }

}
