package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-19 18:20
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "message_record")
public class MessageRecord extends CommonAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
    private String id;

//    private String senderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

//    private String receiverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String message;

    private String type;

    /**
     * 0-消息发送者已发送；1-消息接收者已接收
     */
    private Integer ack;

}
