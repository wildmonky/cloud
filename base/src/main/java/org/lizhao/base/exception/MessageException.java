package org.lizhao.base.exception;

/**
 * Description 捕获该异常，封装为响应返回并打印日志
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-22 19:57
 * @since 0.0.1-SNAPSHOT
 */
public class MessageException extends RuntimeException{

    private long code;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    protected MessageException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MessageException(long code, String message) {
        super(message);
        this.code = code;
    }

    public MessageException(long code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public MessageException(String message, Object... args) {
        super(String.format(message.replace("{}", "%s"), args));
    }

}
