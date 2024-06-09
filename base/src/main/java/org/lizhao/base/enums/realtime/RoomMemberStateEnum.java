package org.lizhao.base.enums.realtime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-07 21:58
 * @since jdk-1.8.0
 */
@Getter
public enum RoomMemberStateEnum {

    IN(0, "已进入"),

    KICKOFF(1, "被踢出");

    private final int code;

    private final String description;


    RoomMemberStateEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static boolean check(int code, @NotNull RoomMemberStateEnum roomMemberEnum) {
        return roomMemberEnum.code == code;
    }

}
