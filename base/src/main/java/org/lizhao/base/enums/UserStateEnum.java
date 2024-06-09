package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-06 17:41
 * @since jdk-1.8.0
 */
@Getter
public enum UserStateEnum {

    BUSY(1, "用户繁忙中");

    private final Integer code;
    private final String description;

    UserStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 检测 code 枚举元素是否匹配
     * @param code
     * @return true code与枚举元素的code相同；
     *         false  code与枚举元素的code不同
     */
    public boolean check(int code) {
        return this.code == code;
    }

}
