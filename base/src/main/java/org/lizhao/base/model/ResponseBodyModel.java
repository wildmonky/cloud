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

    public ResponseBodyModel() {}

    public static <T> ResponseBodyModel<T> of(int code, T result) {
        ResponseBodyModel<T> responseResultModel = new ResponseBodyModel<T>();
        responseResultModel.setCode(code);
        responseResultModel.setResult(result);
        return responseResultModel;
    }

    public static <T> ResponseBodyModel<T> success(T result) {
        return of(1, result);
    }

    public static <T> ResponseBodyModel<T> error(T result) {
        return of(0, result);
    }

}
