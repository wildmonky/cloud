package org.lizhao.base.model.realtime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.Invite;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.model.amqp.UserThing;

/**
 * Description 房间邀请事件
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-13 16:13
 * @since 0.0.1-SNAPSHOT
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class RoomInviteThing extends UserThing {

    private String roomId;

    private String roomName;

    private Room room;

    /**
     * 邀请谁 userId
     */
    private String receiverId;

    private String receiverName;

    private Invite invite;

    public RoomInviteThing(String senderId, String senderName, String receiverId, String receiverName, String type, String message, Boolean handled, String roomId, String roomName, Invite invite) {
        super(senderId, senderName, type, message, handled);
        this.roomId = roomId;
        this.roomName = roomName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.invite = invite;
    }

}
