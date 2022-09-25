package org.lizhao.base.entity.authority;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;

/**
 * Description 权限实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:41
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@TableName("authority")
public class Authority extends AppendInfo {

    /**
     * 权限Id
     */
    @TableId
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限状态：0-初始；1-可用
     */
    private Integer status;
}
