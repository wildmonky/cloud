package org.lizhao.realtime.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.entity.realtime.InviteRecord;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.entity.realtime.RoomMember;
import org.lizhao.base.enums.realtime.*;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.SimpleUserInfo;
import org.lizhao.base.model.UserInfoHolder;
import org.lizhao.base.model.realtime.RoomInviteThing;
import org.lizhao.base.model.realtime.RoomModel;
import org.lizhao.realtime.handler.MessageHandler;
import org.lizhao.realtime.repository.InviteRecordRepository;
import org.lizhao.realtime.repository.RoomMemberRepository;
import org.lizhao.realtime.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private InviteRecordRepository inviteRecordRepository;

    @Resource
    private MessageHandler messageHandler;

    /**
     * 创建房间，并将当前用户设置为房主并加入房间
     * @param room 房间信息
     */
    @Transactional(rollbackFor = Exception.class)
    public RoomModel create(Room room) {
        if (roomRepository.existsRoomByName(room.getName())) {
            throw new MessageException("房间{}已存在，请输入新的房间名", room.getName());
        }

        SimpleUserInfo currentUser = UserInfoHolder.get();
        Room newRoom = new Room();
        newRoom.setName(room.getName());
        newRoom.setOwnerId(currentUser.getId());
        newRoom.setCapacity(room.getCapacity());
        newRoom.setUsed(1);
        if (StringUtils.isNotBlank(room.getSecret())) {
            newRoom.setSecret(room.getSecret());
        }

        newRoom = roomRepository.save(newRoom);

        SimpleUserInfo simpleUserInfo = UserInfoHolder.get();
        RoomMember roomMember = new RoomMember();
        roomMember.setRoomId(newRoom.getId());
        roomMember.setUserId(simpleUserInfo.getId());
        roomMember.setStatus(RoomMemberStateEnum.IN.getCode());

        roomMemberRepository.save(roomMember);
        return new RoomModel(newRoom, roomMember);
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
        SimpleUserInfo currentUser = UserInfoHolder.get();
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
        if (roomHasNotSufficientCapacity(room, 1)) {
            throw new MessageException("{}房间已满员", room.getName());
        }
        RoomMember roomMember = new RoomMember();
        roomMember.setRoomId(room.getId());
        roomMember.setUserId(userInfo.getId());

        return roomMemberRepository.save(roomMember);
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
        if (roomHasNotSufficientCapacity(room, 1)) {
            throw new MessageException("{}房间已满员", room.getName());
        }
        SimpleUserInfo currentUser = UserInfoHolder.get();

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

        InviteRecord inviteRecord = new InviteRecord();
        inviteRecord.setResourceId(room.getId());
        inviteRecord.setResourceType(InviteResourceTypeEnum.ROOM.getCode());
        inviteRecord.setSenderId(currentUser.getId());
        inviteRecord.setSendTime(LocalDateTime.now());
        inviteRecord.setReceiverId(userInfo.getId());
        inviteRecord.setStatus(InviteStateEnum.SENT.getCode());

        inviteRecordRepository.save(inviteRecord);
    }

    /**
     * 邀请多个成员加入房间
     * @param roomName 房间名
     * @param userIds 被邀请人用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void inviteMembers(String roomName, Set<String> userIds) {
        if (userIds == null) {
            throw new MessageException("要添加的房间成员不能为空");
        }

        userIds = userIds.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());

        if (userIds.isEmpty()) {
            throw new MessageException("要添加的房间成员不能为空");
        }

        // TODO user on online ?
        Room room = checkRoom(roomName);
        if (roomHasNotSufficientCapacity(room, userIds.size())) {
            throw new MessageException("{}房间已满员", room.getName());
        }
        SimpleUserInfo currentUser = UserInfoHolder.get();

        Collection<RoomMember> roomMembers = roomMembersInRoom(room.getId(), RoomMemberStateEnum.IN);

        boolean currentUserIsInRoom = false;

        for (RoomMember roomMember : roomMembers) {
            if (!currentUserIsInRoom) {
                currentUserIsInRoom = Objects.equals(roomMember.getUserId(), currentUser.getId());
            }

            if (Objects.equals(RoomMemberStateEnum.KICKOFF.getCode(), roomMember.getStatus())) {
                // 移除已经邀请过的成员
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
        List<InviteRecord> list = new ArrayList<>(userIds.size());
        List<MessageHandler.Message<RoomInviteThing>> messages = new ArrayList<>(userIds.size());
        for (String userId : userIds) {
            InviteRecord inviteRecord = new InviteRecord();
            inviteRecord.setResourceId(room.getId());
            inviteRecord.setResourceType(InviteResourceTypeEnum.ROOM.getCode());
            inviteRecord.setSenderId(currentUser.getId());
            inviteRecord.setSendTime(now);
            inviteRecord.setReceiverId(userId);
            inviteRecord.setStatus(InviteStateEnum.SENT.getCode());
            list.add(inviteRecord);

            messages.add(MessageHandler.Message
                    .buildMessage(currentUser.getId(), userId, new RoomInviteThing(
                            Objects.equals(room.getCapacity(), 2) ? MessageTypeEnum.CALL.name() : MessageTypeEnum.ROOM_INVITE.name(),
                            "",
                            false,
                            room.getId(),
                            room.getName(),
                            currentUser.getId(),
                            userId,
                            inviteRecord)
                    )
            );
        }

        inviteRecordRepository.saveAll(list);

        log.info("向用户发送进入房间{}邀请开始", room.getName());
        messageHandler.sendMessages(messages);
        log.info("向用户发送进入房间{}邀请完成", room.getName());

        log.info("邀请{}用户加入房间{}成功", userIds.toArray(), room.getName());
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
        if (roomHasNotSufficientCapacity(room, 1)) {
            throw new MessageException("{}房间已满员", room.getName());
        }

        SimpleUserInfo currentUser = UserInfoHolder.get();

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
        InviteRecord roomInviteRecord = inviteRecordRepository.findRoomInviteRecord(room.getId(), currentUser.getId());
        if (roomInviteRecord == null) {
            throw new MessageException("{}用户，没有邀请无法加入，请让房主邀请或使用房间密码加入", currentUser.getName());
        }
        // 被踢出，且无新邀请
        if (roomMember !=null
                && RoomMemberStateEnum.KICKOFF.getCode() == roomMember.getStatus()
                && Objects.equals(roomMember.getInviteRecordId(), roomInviteRecord.getId())
        ) {
            throw new MessageException("{}用户，你被踢出，请通知房主再次邀请", currentUser.getName());
        }

        // 拒绝了邀请
        if (InviteResultStateEnum.REFUSE.getCode() == roomInviteRecord.getResult()) {
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
    public boolean roomHasNotSufficientCapacity(Room room, int join) {
        Integer capacity = room.getCapacity();
        Integer used = room.getUsed();
        return capacity - used < join;
    }

    /**
     * 接受加入房间roomName的邀请
     * @param roomName 房间名
     */
    public RoomMember acceptInvite(String roomName, String inviteId) {
        Room room = checkRoom(roomName);
        SimpleUserInfo userInfo = UserInfoHolder.get();
        InviteRecord roomInviteRecord = inviteRecordRepository.findById(inviteId).orElseThrow(() -> new MessageException("不存在{}对应的邀请信息", inviteId));

        if (roomInviteRecord.getResult() == null) {
            roomInviteRecord.setStatus(InviteResultStateEnum.ACCEPT.getCode());
            inviteRecordRepository.save(roomInviteRecord);
            log.info("{}:{}用户接受了加入房间{}的邀请", userInfo.getId(), userInfo.getName(), room.getName());
            return joinRoom(roomName, null);
        }

        InviteResultStateEnum inviteResultStateEnum = InviteResultStateEnum.of(roomInviteRecord.getResult());
        if (inviteResultStateEnum == InviteResultStateEnum.REFUSE) {
            throw new RuntimeException(String.format("来自房间%s:%s的邀请已被拒绝", room.getId(), room.getName()));
        }
        return joinRoom(roomName, null);
    }

    /**
     * 拒绝加入房间roomName的邀请
     * @param roomName 房间名
     */
    public void refuseInvite(String roomName, String inviteId) {
        Room room = checkRoom(roomName);
        SimpleUserInfo userInfo = UserInfoHolder.get();
        InviteRecord roomInviteRecord = inviteRecordRepository.findById(inviteId).orElseThrow(() -> new MessageException("不存在{}对应的邀请信息", inviteId));

        if (roomInviteRecord.getResult() == null) {
            roomInviteRecord.setStatus(InviteResultStateEnum.REFUSE.getCode());
            inviteRecordRepository.save(roomInviteRecord);
            log.info("{}:{}用户拒绝了加入房间{}的邀请", userInfo.getId(), userInfo.getName(), room.getName());
            return;
        }

        InviteResultStateEnum inviteResultStateEnum = InviteResultStateEnum.of(roomInviteRecord.getResult());
        if (inviteResultStateEnum == InviteResultStateEnum.ACCEPT) {
            throw new RuntimeException(String.format("来自房间%s:%s的邀请已被接受", room.getId(), room.getName()));
        }

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
     * 房间中的所有成员
     * @param roomId 房间Id
     * @return 房间成员
     */
    public Collection<RoomMember> allMembersInRoom(String roomId) {
        return roomMemberRepository.findRoomMembersByRoomId(roomId);
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
     * @param status 用户状态,{@link RoomMemberStateEnum}
     * @return id为 roomId 中的用户信息
     */
    public Collection<RoomMember> roomMembersInRoom(String roomId, RoomMemberStateEnum status) {
        return roomMemberRepository.findRoomMembersByRoomIdAndStatus(roomId, status.getCode());
    }

}
