package org.lizhao.base.entity.datastream;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;

import java.math.BigInteger;

/**
 * Description 传输文件实体类 上传/下载
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 17:04
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class TransferFile extends AppendInfo {

    /**
     * 主键
     */
    private BigInteger id;

    /**
     * 文件名称（例如 test.txt 包含后缀）
     */
    private String fileName;

    /**
     * 文件前缀 不包含后缀的文件名称
     */
    private String prefix;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件类型  TODO 待确认 使用枚举类保存
     */
    private String fileType;

    /**
     * 文件路径（系统文件路径，网络文件路径）
     */
    private String filePath;

    /**
     * 文件状态
     */
    private Byte status;

}
