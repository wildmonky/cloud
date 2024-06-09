package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

/**
 * Description 房间成员
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-07 17:02
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "room_member")
public class RoomMember extends CommonAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
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

}
