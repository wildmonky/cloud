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

    USER_MESSAGE(0),
    CALL(1),
    ROOM_INVITE(2);

    private final Integer code;

    MessageTypeEnum(int code) {
        this.code = code;
    }

}
