package org.lizhao.base.model.amqp;

import lombok.Getter;
import lombok.Setter;

/**
 * Description amqp thing model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-13 14:08
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class Thing {

    private String type;

    private String message;

    private Boolean handled;

    public Thing(String type, String message, Boolean handled) {
        this.type = type;
        this.message = message;
        this.handled = handled != null && handled;
    }

    @Override
    public String toString() {
        return "{ type: " + type + ", message: " + message + ", handled: " + handled + " }";
    }

}
