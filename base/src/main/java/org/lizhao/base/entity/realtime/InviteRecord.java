package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

import java.time.LocalDateTime;

/**
 * Description 邀请记录
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-10 14:02
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "invite_record")
public class InviteRecord extends CommonAttribute {

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

    /**
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 发出者id
     */
    private String senderId;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 接收者Id
     */
    private String receiverId;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 邀请状态
     */
    private Integer status;

    /**
     * 邀请结果 {@link org.lizhao.base.enums.realtime.InviteResultStateEnum}
     */
    private Integer result;

    /**
     * 邀请信息，更多描述
     */
    private String message;

}
