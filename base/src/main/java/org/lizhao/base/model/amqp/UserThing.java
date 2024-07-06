package org.lizhao.base.model.amqp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-13 14:13
 * @since 0.0.1-SNAPSHOT
 */
@ToString(callSuper = true)
@Getter
@Setter
public class UserThing extends Thing {

    private String senderId;

    private String senderName;

    public UserThing(String senderId, String senderName, String type, String message, Boolean handled) {
        super(type, message, handled);
        this.senderId = senderId;
        this.senderName = senderName;
    }

}
