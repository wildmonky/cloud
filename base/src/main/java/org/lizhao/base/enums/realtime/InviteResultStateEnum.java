package org.lizhao.base.enums.realtime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Arrays;

/**
 * Description {@link org.lizhao.base.entity.realtime.InviteRecord#getResult()}
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-10 14:09
 * @since jdk-1.8.0
 */
@Getter
public enum InviteResultStateEnum {

    ACCEPT(0, "接受"),
    REFUSE(1, "拒绝");

    private final int code;

    private final String description;

    InviteResultStateEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static InviteResultStateEnum of(@NotNull Integer code) {
//        if (code == null) {
//            throw new RuntimeException("code不能为null");
//        }
        return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElseThrow(() -> new RuntimeException(String.format("没有%s对应的InviteResultStateEnum", code)));
    }

}
