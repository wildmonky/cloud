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
public class ResponseResultModel<T> {

    private int code;

    private T body;

    public ResponseResultModel() {}

    public static <T> ResponseResultModel<T> of(int code, T body) {
        ResponseResultModel<T> responseResultModel = new ResponseResultModel<>();
        responseResultModel.setCode(code);
        responseResultModel.setBody(body);
        return responseResultModel;
    }

    public static <T> ResponseResultModel<T> success(T body) {
        return of(1, body);
    }

    public static <T> ResponseResultModel<T> error(T body) {
        return of(0, body);
    }

}
