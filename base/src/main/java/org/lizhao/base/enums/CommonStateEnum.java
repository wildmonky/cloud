package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description 用户状态枚举类
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-26 21:59
 * @since jdk-1.8.0
 */
@Getter
public enum CommonStateEnum {

    NEW(0, "初创"),
    NORMAL(1, "正常"),
    LOCKED(2,  "锁住");

    private final Integer code;
    private final String description;

    CommonStateEnum(Integer code, String description) {
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
