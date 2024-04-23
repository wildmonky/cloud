package org.lizhao.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 响应结果封装模型
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-08-14 14:13
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class ResponseBodyModel<T> {

    /**
     * 请求状态
     */
    private int code;

    /**
     * 返回值
     */
    private T result;

    /**
     * 信息
     */
    private String message;

    /**
     * csrf request header 应配置值
     */
    private String csrfHeaderToken;

    public ResponseBodyModel() {}

    public static <T> ResponseBodyModel<T> of(int code, T result, String message) {
        ResponseBodyModel<T> responseResultModel = new ResponseBodyModel<T>();
        responseResultModel.setCode(code);
        responseResultModel.setResult(result);
        responseResultModel.setMessage(message);
        return responseResultModel;
    }

    public static <T> ResponseBodyModel<T> success(T result) {
        return of(200, result, null);
    }

    public static <T> ResponseBodyModel<T> success(T result, String message) {
        return of(200, result, message);
    }

    public static <T> ResponseBodyModel<T> error(String message) {
        return of(-1, null, message);
    }

    public void withCsrf(String csrfHeaderToken) {
        this.csrfHeaderToken = csrfHeaderToken;
    }

}
