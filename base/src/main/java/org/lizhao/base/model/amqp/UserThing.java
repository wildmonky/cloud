package org.lizhao.base.model.amqp;

import lombok.Getter;
import lombok.Setter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-13 14:13
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class UserThing extends Thing {

    private String sender;

    public UserThing(String sender, String type, String message, Boolean handled) {
        super(type, message, handled);
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "{ userId: " + this.sender + ", type: " + super.getType() + ", message: " + super.getMessage() + ", handled: " + super.getHandled() + " }";
    }

}
