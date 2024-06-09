package org.lizhao.base.enums.realtime;

import lombok.Getter;

/**
 * Description client 状态枚举
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-08 17:48
 * @since jdk-1.8.0
 */
@Getter
public enum ClientStateEnum {

    ON(0,"on"),
    OFF(1,"off");

    private final int code;
    private final String status;

    ClientStateEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

}
