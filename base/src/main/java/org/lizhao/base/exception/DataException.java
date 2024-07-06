package org.lizhao.base.exception;

/**
 * Description 数据异常
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-07-03 20:45
 * @since 0.0.1-SNAPSHOT
 */
public class DataException extends MessageException{

    public DataException() {
        super();
    }

    public DataException(String message, Object... args) {
        super(message, args);
    }

}
