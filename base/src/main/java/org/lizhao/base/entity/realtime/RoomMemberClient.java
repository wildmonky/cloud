package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

/**
 * Description 房间成员客户端
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 16:54
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "room_member_client")
public class RoomMemberClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
    private String id;

    /**
     * {@link RoomMember#getId()}
     */
    private String roomMemberId;

    /**
     * realtime (播放/推流)客户端id，可用于踢出
     */
    private String clientId;

    /**
     * {@link org.lizhao.base.enums.realtime.ClientActionEnum}
     * 客户端操作：publish、play
     */
    private Integer clientAction;

    /**
     * {@link org.lizhao.base.enums.realtime.PlatformEnum}
     * 客户端类型：srs 或其他
     */
    private String platform;

    /**
     * 当action是play时，才有值，防止被其他人盗链，不对则踢出
     */
    private String playPageUrl;

    /**
     * 当action是publish时，才有值，统计用
     */
    private String publishPageUrl;

    /**
     * {@link org.lizhao.base.enums.realtime.RoomMemberStateEnum}
     */
    private Integer status ;

}
