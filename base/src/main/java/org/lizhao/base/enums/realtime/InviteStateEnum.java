package org.lizhao.base.enums.realtime;

import lombok.Getter;

/**
 * Description {@link org.lizhao.base.entity.realtime.InviteRecord#getStatus()}
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-10 14:15
 * @since jdk-1.8.0
 */
@Getter
public enum InviteStateEnum {

    SENT(0, "已发送"),
    RECEIVED(1, "已接收");


    private final int code;

    private final String description;

    InviteStateEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
