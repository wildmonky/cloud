package org.lizhao.base.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.realtime.Room;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.enums.ResourceUsageEnum;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

import java.time.LocalDateTime;

/**
 * Description 邀请
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-10 14:02
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "invite")
public class Invite extends CommonAttribute {

    /**
     * 邀请记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
    private String id;

    /**
     * 资源id
     * 如：{@link Room#getId()}
     */
    private String resourceId;

    private String resourceName;

    /**
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 用途 {@link ResourceUsageEnum}
     */
    private Integer usage;

    /**
     * 发出者id {@link User#getId()}
     */
//    private String senderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 接收者Id {@link User#getId()}
     */
//    private String receiverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 邀请状态 {@link org.lizhao.base.enums.realtime.InviteStateEnum}
     */
    private Integer status;

    /**
     * 邀请信息，更多描述
     */
    private String message;

}
