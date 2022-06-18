package org.lizhao.base.entity.datastream;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;

import java.math.BigInteger;

/**
 * Description 传输文件的变更记录（重新上传、下载、删除）
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 17:17
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class TransferFileRecord extends AppendInfo {

    /**
     * 主键
     */
    private BigInteger id;

    /**
     * 传输文件Id {@link TransferFile#getId()}
     */
    private BigInteger transferFileId;

    /**
     * 文件操作类型
     */
    private Integer operationType;

}
