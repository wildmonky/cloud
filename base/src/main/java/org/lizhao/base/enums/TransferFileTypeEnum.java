package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description 传输文件类型
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2022-06-18 17:48
 * @since jdk-1.8.0
 * @see org.lizhao.base.entity.datastream.TransferFile
 * @see org.lizhao.base.entity.datastream.TransferFileRecord
 */
@Getter
public enum TransferFileTypeEnum {

    /**
     * 传输文件类型
     */
    TXT(1, "", "文本"),
    EXCEL(2, "", "excel"),
    WORD(3, "", "word");

    private final Integer id;
    private final String contentType;
    private final String type;

    TransferFileTypeEnum(Integer id, String contentType, String type) {
        this.id = id;
        this.contentType = contentType;
        this.type = type;
    }

}
