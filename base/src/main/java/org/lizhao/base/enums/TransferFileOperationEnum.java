package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description 传输文件操作枚举类
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2022-06-18 17:26
 * @since jdk-1.8.0
 * @see org.lizhao.base.entity.datastream.TransferFile
 * @see org.lizhao.base.entity.datastream.TransferFileRecord
 */
@Getter
public enum TransferFileOperationEnum {

    //传输文件操作枚举项
    UPLOAD(1, "上传文件"),
    UPDATE(2, "重新上传"),
    DELETE(3, "删除文件");


    private final Integer operationId;

    private final String operationName;

    TransferFileOperationEnum(Integer operationId, String operationName) {
        this.operationId = operationId;
        this.operationName = operationName;
    }

}
