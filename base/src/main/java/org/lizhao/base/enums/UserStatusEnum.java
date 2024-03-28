package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-26 21:59
 * @since jdk-1.8.0
 */
@Getter
public enum UserStatusEnum {

    NEW(0, "初创"),
    NORMAL(1, "正常"),
    LOCK(2,  "锁住");

    private final Integer code;
    private final String description;

    UserStatusEnum(Integer code,String description) {
        this.code = code;
        this.description = description;
    }

}
