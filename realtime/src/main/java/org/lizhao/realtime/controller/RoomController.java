package org.lizhao.realtime.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.realtime.InviteModel;
import org.lizhao.base.model.realtime.RoomModel;
import org.lizhao.realtime.service.RoomService;
import org.springframework.web.bind.annotation.*;

/**
 * Description room controller 聊天室
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
    @PatchMapping("/{roomName}/secret/{secret}")
    public void resetSecret(@PathVariable("roomName")String roomName, @PathVariable("secret")String secret) {
        roomService.resetSecretWithinRoom(roomName, secret);
    }

    /**
     * 解散聊天室：
     * 删除房间 并 清空所有房间成员
     * @param roomName 房间名
     */
    @DeleteMapping("/{roomName}")
    public void remove(@PathVariable("roomName")String roomName) {
        roomService.remove(roomName);
    }

    /**
     * 房间成员邀请 用户
     * @param roomName 房间名
     */
    @PutMapping("/{roomName}/member")
    public void invite(@PathVariable("roomName")String roomName, @RequestBody InviteModel inviteModel) {
        roomService.inviteMembers(roomName, inviteModel.getUserIds(), inviteModel.getUsage());
    }

    /**
     * 接收房间邀请
     * @param roomName 房间名
     */
    @PutMapping("/{roomName}/invite/{inviteId}")
    public RoomModel acceptInvite(@PathVariable("roomName") String roomName, @PathVariable("inviteId") String inviteId) {
        return roomService.acceptInvite(roomName, inviteId);
    }

    /**
     * 拒绝房间邀请
     * @param roomName 房间名
     */
    @DeleteMapping("/{roomName}/invite/{inviteId}")
    public void refuseInvite(@PathVariable("roomName") String roomName, @PathVariable("inviteId") String inviteId) {
        roomService.refuseInvite(roomName, inviteId);
        // TODO send refuse message
    }

    /**
     * 用户自己尝试加入房间
     * @param roomName 房间名
     * @param secret 房间密码
     */
    @PutMapping("/user/{roomName}/{secret}")
    public RoomModel join(@PathVariable("roomName")String roomName, @PathVariable(value = "secret", required = false)String secret) {
        return roomService.roomInfo(roomService.joinRoom(roomName, secret));
    }

    /**
     * 删除房间成员
     *
     * @author lizhao
     * @date 2024/7/2 16:58
     * @param roomName 房间名
     * @param memberId 成员id
     * @return org.lizhao.base.model.realtime.RoomModel
     */
    @PatchMapping("{roomName}/member/{memberId}")
    public RoomModel reduceMember(@PathVariable("roomName")String roomName, @PathVariable("memberId")String memberId) {
        return roomService.reduceMember(roomName, null, memberId);
    }

    /**
     * 当前用户离开房间，并保留房间
     *
     * @author lizhao
     * @date 2024/7/2 16:58
     * @param roomName 房间名
     * @return org.lizhao.base.model.realtime.RoomModel
     */
    @PatchMapping("{roomName}/member/keepRoom")
    public RoomModel leaveRoomAndKeepRoom(@PathVariable("roomName")String roomName) {
        return roomService.selfLeaveRoom(roomName);
    }

    /**
     * 当前用户离开房间，当房间无成员时释放房间
     *
     * @author lizhao
     * @date 2024/7/2 16:58
     * @param roomName 房间名
     */
    @PatchMapping("{roomName}/member/releaseRoomWhenEmpty")
    public RoomModel leaveRoomAndReleaseRoomWhenRoomIsEmpty(@PathVariable("roomName")String roomName) {
        return roomService.leaveRoomAndReleaseRoomWhenRoomIsEmpty(roomName);
    }


}
