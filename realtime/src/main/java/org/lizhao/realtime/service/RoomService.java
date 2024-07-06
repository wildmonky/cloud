package org.lizhao.realtime.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.entity.Invite;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.entity.realtime.RoomMember;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.enums.ResourceUsageEnum;
import org.lizhao.base.enums.realtime.InviteResourceTypeEnum;
import org.lizhao.base.enums.realtime.InviteStateEnum;
import org.lizhao.base.enums.realtime.RoomMemberStateEnum;
import org.lizhao.base.exception.DataException;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.model.UserInfoHolder;
import org.lizhao.base.model.realtime.RoomMemberModel;
import org.lizhao.base.model.realtime.RoomModel;
import org.lizhao.realtime.handler.MessageHandler;
import org.lizhao.realtime.handler.UserHandler;
import org.lizhao.realtime.repository.InviteRepository;
import org.lizhao.realtime.repository.RoomMemberRepository;
import org.lizhao.realtime.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description
 * {@link RoomService#create}(创建新的房间)
 * {@link RoomService#inviteMember}(邀请加入房间)
 * {@link RoomService#joinRoom}(接受邀请，加入房间)
 * {@link RoomService#resetOwner}(重置房主)
 * {@link RoomService#resetSecretWithinRoom}(重置房间密码)
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-07 21:25
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
public class RoomService {

    @Resource
    private RoomRepository roomRepository;

    @Resource
    private RoomMemberRepository roomMemberRepository;

    @Resource
    private InviteRepository inviteRepository;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private UserHandler userHandler;

    /**
     * 创建房间，并将当前用户设置为房主并加入房间
     * @param room 房间信息
     */
    @Transactional(rollbackFor = Exception.class)
    public RoomModel create(Room room) {
        if (roomRepository.existsRoomByName(room.getName())) {
            throw new MessageException("房间{}已存在，请输入新的房间名", room.getName());
        }

        UserInfo currentUser = UserInfoHolder.get();
        Room newRoom = new Room();
        newRoom.setName(room.getName());
        newRoom.setOwnerId(currentUser.getId());
        newRoom.setCapacity(room.getCapacity());
        newRoom.setUsed(1);
        if (StringUtils.isNotBlank(room.getSecret())) {
            newRoom.setSecret(room.getSecret());
        }

        newRoom = roomRepository.save(newRoom);

        UserInfo simpleUserInfo = UserInfoHolder.get();
        RoomMember roomMember = new RoomMember();
        roomMember.setRoomId(newRoom.getId());
        roomMember.setUserId(simpleUserInfo.getId());
        roomMember.setStatus(RoomMemberStateEnum.IN.getCode());

        roomMemberRepository.save(roomMember);
        return new RoomModel(newRoom, roomMember, Collections.emptyList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(String roomName) {
        Room room = checkRoom(roomName);
        roomMemberRepository.deleteRoomMembersByRoomId(room.getId());
        roomRepository.deleteRoomByName(roomName);
    }

    /**
     * 重置房主
     * @param roomName 房间名
     * @param userInfo 房主信息
     */
    public void resetOwner(String roomName, SimpleUserInfo userInfo) {
        UserInfo currentUser = UserInfoHolder.get();
        Room room = checkRoom(roomName);

        if (!Objects.equals(room.getOwnerId(), currentUser.getId())) {
            throw new MessageException("{}用户，不是{}房间的房主，不能设置房主");
        }

        RoomMember roomMember = roomMemberInRoom(room.getId(), userInfo.getId());
        if (roomMember == null) {
            throw new MessageException("{}用户不是｛｝房间的成员，不能设置为房主", userInfo.getName(), room.getName());
        }

        if (Objects.equals(RoomMemberStateEnum.KICKOFF.getCode(), roomMember.getStatus())) {
            throw new MessageException("{}用户被踢出｛｝房间，不能成为房主", userInfo.getName(), room.getName());
        }

        room.setOwnerId(userInfo.getId());
        roomRepository.save(room);
        log.info("{}重置{}房间的房主为｛｝", currentUser.getName(), userInfo.getName());
    }

    /**
     * 重置房间密码
     * @param roomName 房间名
     * @param secret 密码
     */
    public void resetSecretWithinRoom(String roomName, String secret) {
        if (StringUtils.isBlank(roomName)) {
            throw new MessageException("新的房间名不能为空");
        }

        Room room = roomRepository.findRoomByName(roomName);
        if (room == null) {
            throw new MessageException("{}对应的房间不存在", roomName);
        }

        room.setSecret(secret);
        roomRepository.save(room);
    }

    /**
     * 向房间中添加成员
     * @param roomName 房间名
     * @param userInfo 要添加的用户信息
     */
    public RoomMember addMember(String roomName, SimpleUserInfo userInfo) {
        if (StringUtils.isBlank(userInfo.getId())) {
            throw new MessageException("要添加的房间成员id不能为空");
        }
        // TODO check: is user on online ?
        Room room = checkRoom(roomName);
        if (roomHasSufficientCapacity(room, 1)) {
            throw new MessageException("{}房间已满员", room.getName());
        }
        RoomMember roomMember = new RoomMember();
        roomMember.setRoomId(room.getId());
        roomMember.setUserId(userInfo.getId());
        roomMember.setStatus(RoomMemberStateEnum.IN.getCode());

        return roomMemberRepository.save(roomMember);
    }

    /**
     * 删除房间中的指定成员
     *
     * @param roomName 房间名
     * @param userId 用户Id
     * @param memberId 成员Id
     */
    public RoomModel reduceMember(String roomName, String userId, String memberId) {
        Room room = checkRoom(roomName);
        Collection<RoomMember> roomMembers = roomMemberRepository.findRoomMembersByRoomId(room.getId());

        RoomMember existedRoomMember = roomMembers.stream().filter(roomMember -> Objects.equals(userId, roomMember.getUserId()) || Objects.equals(memberId, roomMember.getId())).findFirst().orElseThrow(() -> new MessageException("{}中未查询到{}对应的yoghurt成员", roomName, memberId));
        roomMembers.remove(existedRoomMember);
        roomMemberRepository.deleteById(existedRoomMember.getId());

        room.setUsed(room.getUsed() - 1);
        roomRepository.save(room);

        return new RoomModel(room, null, roomMembers);
    }

    /**
     * 房间中的成员 离开
     *
     * @param roomName 房间名
     * @param userId 用户Id
     * @param memberId 成员Id
     */
    public RoomModel memberLeave(String roomName, String userId, String memberId) {
        Room room = checkRoom(roomName);
        Collection<RoomMember> roomMembers = roomMemberRepository.findRoomMembersByRoomId(room.getId());

        RoomMember existedRoomMember = roomMembers.stream().filter(roomMember -> Objects.equals(userId, roomMember.getUserId()) || Objects.equals(memberId, roomMember.getId())).findFirst().orElse(null);
        // 对应用户或成员，不是房间的成员
        if (existedRoomMember == null) {
            log.warn("用户{}，成员{}，不在房间{}中", userId, memberId, roomName);
            return null;
        }
        existedRoomMember.setStatus(RoomMemberStateEnum.OUT.getCode());
        roomMemberRepository.save(existedRoomMember);

        room.setUsed(room.getUsed() - 1);
        roomRepository.save(room);

        return new RoomModel(room, null, roomMembers);
    }

    /**
     * 当前用户加入房间<br/>
     * 1、房主直接加入；<br/>
     * 2、房间无需密码，直接加入；<br/>
     * 3、房间配置了密码，传入secret不为空，优先使用密码加入；<br/>
     * 4、房间配置了密码，传入secret为空， 检查是否有邀请，被踢出的用户需要新的邀请；<br/>
     * @param roomName 房间名
     * @param secret 房间密码
     */
    public RoomMember joinRoom(String roomName, String secret) {
        Room room = checkRoom(roomName);
        if (roomHasSufficientCapacity(room, 1)) {
            throw new MessageException("{}房间已满员", room.getName());
        }

        UserInfo currentUserInfo = UserInfoHolder.get();
        SimpleUserInfo currentUser = currentUserInfo.transferToSimple();

        // 房主重连
        if (Objects.equals(room.getOwnerId(), currentUser.getId())) {
            return addMember(roomName, currentUser);
        }

        // room未配置密码，任何人都可以加入
        if (StringUtils.isBlank(room.getSecret())) {
            return addMember(roomName, currentUser);
        }

        // secret不为空时，优先使用密码加入房间
        if (StringUtils.isNotBlank(secret) && Objects.equals(room.getSecret(), secret)) {
            return addMember(roomName, currentUser);
        }

        // room 配置了密码，需要验证是否可以加入
        // 用户是否已经是房间成员
        RoomMember roomMember= roomMemberInRoom(room.getId(), currentUser.getId());
        // 查询是否有邀请信息
        Invite roomInvite = inviteRepository.findRoomInvite(room.getId(), currentUser.getId());
        if (roomInvite == null) {
            throw new MessageException("{}用户，没有邀请无法加入，请让房主邀请或使用房间密码加入", currentUser.getName());
        }
        // 被踢出，且无新邀请
        if (roomMember !=null
                && RoomMemberStateEnum.KICKOFF.getCode() == roomMember.getStatus()
                && Objects.equals(roomMember.getInviteRecordId(), roomInvite.getId())
        ) {
            throw new MessageException("{}用户，你被踢出，请通知房主再次邀请", currentUser.getName());
        }

        // 拒绝了邀请
        if (InviteStateEnum.REFUSE.getCode() == roomInvite.getStatus()) {
            throw new MessageException("{}用户，你拒绝了邀请，无法加入，请通知房主再次邀请");
        }

        return addMember(roomName, currentUser);
    }

    /**
     * 房间是否有足够的容量
     * @param room 房间名
     * @param join 加入人数
     * @return false-可加入；true-房间剩余可加入人数不足
     */
    public boolean roomHasSufficientCapacity(Room room, int join) {
        Integer capacity = room.getCapacity();
        Integer used = room.getUsed();
        return capacity - used < join;
    }

    /**
     * 邀请成员加入房间
     * @param roomName 房间名
     * @param userInfo 邀请的用户
     */
    public void inviteMember(String roomName, SimpleUserInfo userInfo) {
        if (StringUtils.isBlank(userInfo.getId())) {
            throw new MessageException("要添加的房间成员id不能为空");
        }
        // TODO user on online ?
        Room room = checkRoom(roomName);
        if (roomHasSufficientCapacity(room, 1)) {
            throw new MessageException("{}房间已满员", room.getName());
        }
        UserInfo currentUserInfo = UserInfoHolder.get();
        SimpleUserInfo currentUser = currentUserInfo.transferToSimple();

        Collection<RoomMember> roomMembers = roomMembersInRoom(room.getId(), RoomMemberStateEnum.IN);

        boolean currentUserIsInRoom = false;
        boolean addUserIsInRoom = false;

        for (RoomMember roomMember : roomMembers) {
            if (!currentUserIsInRoom) {
                currentUserIsInRoom = Objects.equals(roomMember.getUserId(), currentUser.getId());
            }

            if (!addUserIsInRoom) {
                addUserIsInRoom = Objects.equals(roomMember.getUserId(), userInfo.getId());
            }
        }

        if (!currentUserIsInRoom) {
            throw new MessageException(String.format("当前用户%s，不在%s房间中，无权邀请成员", currentUser.getName(), room.getName()));
        }

        if (addUserIsInRoom) {
            return;
        }

        Invite invite = new Invite();
        invite.setResourceId(room.getId());
        invite.setResourceType(InviteResourceTypeEnum.ROOM.getCode());
        invite.setSendTime(LocalDateTime.now());
        //sender
        User sender = new User();
        sender.setId(currentUser.getId());
        invite.setSender(sender);

        User receiver = new User();
        receiver.setId(userInfo.getId());
        invite.setReceiver(receiver);

        invite.setStatus(InviteStateEnum.SENT.getCode());

        inviteRepository.save(invite);
    }

    /**
     * 邀请多个成员加入房间
     * @param roomName 房间名
     * @param userIds 被邀请人用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void inviteMembers(String roomName, Set<String> userIds, String usage) {
        if (userIds == null) {
            throw new MessageException("要添加的房间成员不能为空");
        }

        // 过滤掉无效的用户id
        userIds = userIds.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            throw new MessageException("要添加的房间成员不能为空");
        }

        UserInfo currentUserInfo = UserInfoHolder.get();
        SimpleUserInfo currentUser = currentUserInfo.transferToSimple();
        // 验证房间信息
        Room room = checkRoom(roomName);
        if (roomHasSufficientCapacity(room, userIds.size())) {
            throw new MessageException("{}房间已满员", room.getName());
        }

        // TODO 验证待加入成员信息
        Collection<User> waitInvitedUsers = userHandler.searchUsersByIds(userIds);
        if (waitInvitedUsers.isEmpty()) {
            throw new RuntimeException("未查询到" + userIds + "对应的用户信息");
        }
        Map<String, User> waitInvitedUserMap = waitInvitedUsers.stream().collect(Collectors.toMap(User::getId, u -> u, (u1, u2) -> u1));

        Collection<RoomMember> roomMembers = roomMembersInRoom(room.getId(), null);

        boolean currentUserIsInRoom = false;

        for (RoomMember roomMember : roomMembers) {
            if (!currentUserIsInRoom) {
                currentUserIsInRoom = Objects.equals(roomMember.getUserId(), currentUser.getId())
                        && Objects.equals(RoomMemberStateEnum.IN.getCode(), roomMember.getStatus());
            }

            if (Objects.equals(RoomMemberStateEnum.IN.getCode(), roomMember.getStatus())) {
                // 移除已经进入房间的成员
                userIds.remove(roomMember.getUserId());
            }
        }

        if (!currentUserIsInRoom) {
            throw new MessageException(String.format("当前用户%s，不在%s房间中，无权邀请成员", currentUser.getName(), room.getName()));
        }

        if (userIds.isEmpty()) {
            log.info("用户都已邀请");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        List<Invite> invites = new ArrayList<>(userIds.size());
        List<MessageHandler.Message<Invite>> messages = new ArrayList<>(userIds.size());
        List<RoomMember> joiningRoomMembers = new ArrayList<>(userIds.size());

        User sender = new User();
        sender.setId(currentUser.getId());
        sender.setName(currentUser.getName());
        for (String userId : userIds) {
            User receiver = new User();
            receiver.setId(userId);

            // 构建 邀请函
            Invite invite = new Invite();
            invite.setResourceId(room.getId());
            invite.setResourceName(room.getName());
            invite.setResourceType(InviteResourceTypeEnum.ROOM.getCode());
            invite.setSendTime(now);
            invite.setSender(sender);
            invite.setReceiver(receiver);
            invite.setReceiver(waitInvitedUserMap.get(userId));
            invite.setStatus(InviteStateEnum.SENT.getCode());
            invite.setUsage(ResourceUsageEnum.ofName(usage).getCode());
            invites.add(invite);

            // 构建要发送的邀请信息
            messages.add(buildInviteMessage(invite));
        }
        // 保存邀请函
        inviteRepository.saveAll(invites);

        // userId----inviteId
        Map<String, String> inviteMap = invites.stream().collect(Collectors.toMap(e -> e.getSender().getId(), Invite::getId));

        for (String userId : userIds) {
            // 构建被邀请的房间成员
            RoomMember joiningRoomMember = new RoomMember();
            joiningRoomMember.setRoomId(room.getId());
            joiningRoomMember.setUserId(userId);
            joiningRoomMember.setStatus(RoomMemberStateEnum.INVITED.getCode());
            joiningRoomMember.setInviteRecordId(inviteMap.get(userId));
//            joiningRoomMember.setComment("");
            joiningRoomMembers.add(joiningRoomMember);
        }
        // 被保存的房间成员
        roomMemberRepository.saveAll(joiningRoomMembers);

        log.info("开始向用户发送进入房间{}邀请", room.getName());
        messageHandler.sendMessages(messages);
    }

    private MessageHandler.Message<Invite> buildInviteMessage(Invite invite) {
        User sender = invite.getSender();
        User receiver = invite.getReceiver();
        return MessageHandler.Message.buildMessage(
                sender.getId(),
                sender.getName(),
                receiver.getId(),
                receiver.getName(),
                invite
        );
    }

    private <T> List<MessageHandler.Message<T>> buildMessages(MessageHandler.Message<T> message, Set<String> userIds) {
        List<MessageHandler.Message<T>> messages = new ArrayList<>(userIds.size());
        for (String userId : userIds) {
            MessageHandler.Message<T> newMessage = message.clone();
            newMessage.setReceiverId(userId);
            messages.add(newMessage);
        }
        return messages;
    }

    /**
     * 接受加入房间roomName的邀请
     * @param roomName 房间名
     */
    public RoomModel acceptInvite(String roomName, String inviteId) {
        Room room = checkRoom(roomName);
        UserInfo currentUser = UserInfoHolder.get();
        SimpleUserInfo userInfo = currentUser.transferToSimple();
        Invite roomInvite = inviteRepository.findById(inviteId).orElseThrow(() -> new MessageException("不存在{}对应的邀请信息", inviteId));

        InviteStateEnum inviteStateEnum = null;
        if (roomInvite.getStatus() != null) {
            inviteStateEnum = InviteStateEnum.of(roomInvite.getStatus());
            if (inviteStateEnum == InviteStateEnum.REFUSE) {
                throw new RuntimeException(String.format("来自房间%s:%s的邀请已被拒绝", room.getId(), room.getName()));
            }
        }

        Collection<RoomMember> roomMembers = roomMemberRepository.findRoomMembersByRoomId(room.getId());
        if (inviteStateEnum == InviteStateEnum.ACCEPT) {
            RoomMember self = roomMembers.stream().filter(roomMember ->
                    !Objects.equals(roomMember.getStatus(), RoomMemberStateEnum.KICKOFF.getCode())
                    && Objects.equals(roomMember.getUserId(), roomInvite.getReceiver().getId())
            ).findFirst().orElseThrow(() -> new DataException("用户{}已经接收邀请，但在房间{}中未发现", userInfo.getName(), room.getName()));
            return new RoomModel(room, self, roomMembers);
        }

        RoomMember self = roomMembers.stream()
                .filter(e -> StringUtils.equals(e.getUserId(), userInfo.getId()))
                .findFirst()
                .orElseThrow(() -> new DataException("未查询到{}:{}在房间{}中的成员信息", userInfo.getId(), userInfo.getName(), room.getName()));
        // 邀请状态变更
        roomInvite.setStatus(InviteStateEnum.ACCEPT.getCode());
        inviteRepository.save(roomInvite);
        // 房间成员状态变更
        self.setStatus(RoomMemberStateEnum.IN.getCode());
        roomMemberRepository.save(self);

        log.info("id为{}的invite已被接受：{}", inviteId, roomInvite);
        log.info("{}:{}用户接受了加入房间{}的邀请", userInfo.getId(), userInfo.getName(), room.getName());
//        self = joinRoom(roomName, null);

        MessageHandler.Message<RoomModel> acceptedMessage = MessageHandler.Message.buildMessage(roomInvite.getSender().getId(), "", roomInvite.getReceiver().getId(), "", "NewRoomMember", new RoomModel(room, self, roomMembers));
        messageHandler.sendMessage(roomInvite.getSender().getId(), acceptedMessage);
        log.info("已通知邀请人{}，邀请{}被接受", roomInvite.getSender().getId(), roomInvite.getId());

        return new RoomModel(room, self, roomMembers);
    }

    /**
     * 拒绝加入房间roomName的邀请
     * @param roomName 房间名
     */
    @Transactional(rollbackFor = Exception.class)
    public void refuseInvite(String roomName, String inviteId) {
        Room room = checkRoom(roomName);
        UserInfo currentUser = UserInfoHolder.get();
        SimpleUserInfo userInfo = currentUser.transferToSimple();
        Invite roomInvite = inviteRepository.findById(inviteId).orElseThrow(() -> new MessageException("不存在{}对应的邀请信息", inviteId));

        if (roomInvite.getStatus() != null) {
            if (Objects.equals(roomInvite.getStatus(), InviteStateEnum.REFUSE.getCode())) {
                return;
            }
            log.info("id为{}的邀请已经被处理了，不能拒绝", inviteId);
            throw new MessageException("该邀请已被使用，无法拒绝");
        }

        roomInvite.setStatus(InviteStateEnum.REFUSE.getCode());
        inviteRepository.save(roomInvite);
        log.info("{}:{}用户拒绝了加入房间{}的邀请", userInfo.getId(), userInfo.getName(), room.getName());
        MessageHandler.Message<Invite> refusedMessage = MessageHandler.Message.buildMessage(roomInvite.getSender().getId(), "", roomInvite.getReceiver().getId(), "", roomInvite);
        messageHandler.sendMessage(roomInvite.getSender().getId(), refusedMessage);
        log.info("已通知邀请人{}，邀请{}被拒绝", roomInvite.getSender().getId(), roomInvite);
    }

    /**
     * 检查房间
     * @param roomName 房间名
     * @return 房间信息
     */
    public Room checkRoom(String roomName) {
        if (StringUtils.isBlank(roomName)) {
            throw new MessageException("roomName 为空");
        }
        Room room = roomRepository.findRoomByName(roomName);
        if (room == null) {
            throw new MessageException("{}对应的房间不存在", roomName);
        }
        return room;
    }

    /**
     * 房间中的用户信息
     *
     * @param roomId 房间id
     * @param userId 用户id
     * @return userId 对应的用户在 id为 roomId 中的 数据
     */
    public RoomMember roomMemberInRoom(String roomId, String userId) {
        return roomMemberRepository.findRoomMemberByRoomIdAndUserId(roomId, userId);
    }

    /**
     * 房间中的用户信息
     *
     * @param roomId 房间id
     * @param status 用户状态:
     *              1、null 查询房间中的所有成员;
     *              2、{@link RoomMemberStateEnum} 查询房间中符合状态的成员;
     * @return id为 roomId 中的用户信息
     */
    public Collection<RoomMember> roomMembersInRoom(String roomId, RoomMemberStateEnum status) {
        if (status == null) {
            return roomMemberRepository.findRoomMembersByRoomId(roomId);
        } else {
            return roomMemberRepository.findRoomMembersByRoomIdAndStatus(roomId, status.getCode());
        }
    }

    public RoomModel roomInfo(RoomMember roomMember) {
        if (roomMember == null) {
            throw new RuntimeException("roomMember 不能为空");
        }

        String roomId = roomMember.getRoomId();
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("房间成员" + roomMember.getId() + "所在的房间" + roomMember.getRoomId() + "不存在"));
        Collection<RoomMember> roomMembers = roomMemberRepository.findRoomMembersByRoomId(room.getId());

        return new RoomModel(room, roomMember, roomMembers);
    }

    /**
     * 用户成员自离开房间
     *
     * @param roomName 房间名
     * @return 房间信息
     */
    @Transactional(rollbackFor = Exception.class)
    public RoomModel selfLeaveRoom(String roomName) {
        UserInfo userInfo = UserInfoHolder.get();
        RoomModel roomModel = memberLeave(roomName, userInfo.getId(), null);
        // TODO 通知其他成员 已离开
        Collection<RoomMemberModel> members = roomModel.getMembers();
        Set<String> userIds = members.stream().filter(m ->
                Objects.equals(m.getStatus(), RoomMemberStateEnum.IN.getCode())
                && !StringUtils.equals(m.getUserId(), userInfo.getId())
        ).map(RoomMemberModel::getUserId).collect(Collectors.toSet());
        // 房间所有成员都已经离开
        if (userIds.isEmpty()) {
            return roomModel;
        }
        MessageHandler.Message<RoomModel> message = MessageHandler.Message.buildMessage(
                userInfo.getId(),
                userInfo.getName(),
                null,
                null,
                "RoomMemberLeave",
                roomModel
                );
        messageHandler.sendMessages(buildMessages(message, userIds));
        return roomModel;
    }

    /**
     * 当房间没有成员时，自动释放房间
     * usage:
     *  1、挂断电话
     *
     * @param roomName 房间名
     */
    @Transactional(rollbackFor = Exception.class)
    public RoomModel leaveRoomAndReleaseRoomWhenRoomIsEmpty(String roomName) {
        RoomModel roomModel = selfLeaveRoom(roomName);
        Collection<RoomMemberModel> members = roomModel.getMembers();
        if (ObjectUtils.isEmpty(members)
            || members.stream().noneMatch(e -> Objects.equals(e.getStatus(), RoomMemberStateEnum.IN.getCode()))) {
            roomMemberRepository.deleteRoomMembersByRoomId(roomModel.getId());
            roomRepository.deleteRoomByName(roomName);
        }
        return roomModel;
    }

}
