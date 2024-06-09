package org.lizhao.base.enums.realtime;

import lombok.Getter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-13 18:41
 * @since jdk-1.8.0
 */
@Getter
public enum MessageTypeEnum {

    USER_MESSAGE("user_message"),
    CALL("call"),
    ROOM_INVITE("room_invite");

    private final String name;

    MessageTypeEnum(String name) {
        this.name = name;
    }

}
