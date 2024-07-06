package org.lizhao.base.enums.realtime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.lizhao.base.entity.Invite;

import java.util.Arrays;

/**
 * Description {@link Invite#getStatus()}
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-10 14:15
 * @since jdk-1.8.0
 */
@Getter
public enum InviteStateEnum {

    CREATE(0, "已创建"),
    SENT(1, "已发送"),
    RECEIVED(2, "已接收"),
    ACCEPT(3, "接收"),
    REFUSE(4, "拒绝");


    private final int code;

    private final String description;

    InviteStateEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static InviteStateEnum of(@NotNull Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElseThrow(() -> new RuntimeException(String.format("没有%s对应的InviteResultStateEnum", code)));
    }

}
