package org.lizhao.base.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

/**
 * Description 基础操作枚举
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 17:30
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public enum BaseOperationEnum {

    /**
     * 通用操作枚举项
     */
    SEARCH("GET", "检索"),
    CREATE("POST", "创建"),
    UPDATE("PUT", "更新"),
    DELETE("DELETE", "删除"),
    GRANT("PATCH", "授权");

    /**
     * 操作Id
     */
    private final String code;
    /**
     * 操作名称
     */
    private final String name;

    BaseOperationEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BaseOperationEnum of(HttpMethod httpMethod) {
        String method = httpMethod.name();
        return of(method);
    }

    public static BaseOperationEnum of(String method) {
        if (StringUtils.isBlank(method)) {
            throw new RuntimeException("参数不能为空");
        }
        for (BaseOperationEnum operation : values()) {
            if (operation.getCode().equalsIgnoreCase(method)) {
                return operation;
            }
        }
        throw new RuntimeException("不存在" + method + "对应的操作");
    }

}
