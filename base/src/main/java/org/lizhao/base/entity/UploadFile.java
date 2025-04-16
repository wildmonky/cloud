package org.lizhao.base.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@TableName("upload_file")
public class UploadFile {

    // 上传文件id
    @TableId
    private String id;

    // 上传文件名
    private String name;

    // 上传文件后缀
    private String suffix;

    /**
     * 保存在服务器的dir文件夹中，使用{@code dir}/{@code name}.{@code suffix}来判断是否存在同名文件
     */
    private String dir;

    /**
     * {@code uri} = {@code dir} + randomFileName <br/>
     * 上传文件在服务器的地址，如 /data/upload/test.xlsx
     */
    private String uri;

    // 上传时间
    private LocalDateTime uploadTime;

}
