package org.lizhao.base.enums.realtime;

import lombok.Getter;

/**
 * Description 客户端类型
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 17:04
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public enum ClientActionEnum {

    PUBLISH(0,"publish"),
    PLAY(1,"play"),
    STOP(2, "stop"),
    UNPUBLISH(3, "unpublish");

    private final int code;
    private final String action;

    ClientActionEnum(int code, String action) {
        this.code = code;
        this.action = action;
    }

}
