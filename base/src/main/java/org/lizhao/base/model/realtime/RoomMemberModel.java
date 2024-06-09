package org.lizhao.base.model.realtime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.realtime.InviteRecord;
import org.lizhao.base.entity.realtime.RoomMember;

/**
 * Description room member model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-10 19:53
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class RoomMemberModel {

    private String id;

    private String roomId;

    private String userId;

    /**
     * 通过{@link InviteRecord#getId()}邀请加入房间
     */
    private String inviteRecordId;

    /**
     * {@link org.lizhao.base.enums.realtime.RoomMemberStateEnum}
     */
    private Integer status;

    /**
     * 备注
     */
    private String comment;

    public RoomMemberModel(@NotNull RoomMember roomMember) {
        this.id = roomMember.getId();
        this.roomId = roomMember.getRoomId();
        this.userId = roomMember.getUserId();
        this.inviteRecordId = roomMember.getInviteRecordId();
        this.status = roomMember.getStatus();
        this.comment = roomMember.getComment();
    }

}
