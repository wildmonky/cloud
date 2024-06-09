package org.lizhao.base.enums.realtime;

import lombok.Getter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-10 15:19
 * @since jdk-1.8.0
 */
@Getter
public enum InviteResourceTypeEnum {

    /**
     * {@link org.lizhao.base.entity.realtime.Room}
     */
    ROOM(0, "房间邀请");

    private final int code;

    private final String description;

    InviteResourceTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
