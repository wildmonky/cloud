package org.lizhao.base.enums;

import lombok.Getter;

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
    UPDATE(1, "更新"),
    DELETE(2, "删除");

    /**
     * 操作Id
     */
    private final Integer operationId;
    /**
     * 操作名称
     */
    private final String operationName;

    BaseOperationEnum(Integer operationId, String operationName) {
        this.operationId = operationId;
        this.operationName = operationName;
    }


}
