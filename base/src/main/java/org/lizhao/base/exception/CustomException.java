package org.lizhao.base.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.lizhao.base.enums.ExceptionEnum;

/**
 * Description 自定义异常
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-10-02 16:38
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class CustomException extends Exception{

    public static CustomException NOT_LOGIN = new CustomException(ExceptionEnum.NOT_LOGIN.getCode(), ExceptionEnum.NOT_LOGIN.getMessage());

    private long code;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    protected CustomException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CustomException(long code, String message) {
        super(message);
        this.code = code;
    }

}
