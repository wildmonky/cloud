package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description 异常枚举
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2022-10-02 17:10
 * @since jdk-1.8.0
 */
@Getter
public enum ExceptionEnum {

    NOT_LOGIN(601, "用户未登录");

    private long code;
    private String message;

    ExceptionEnum(long code, String message) {
        this.code = code;
        this.message = message;
    }

}
