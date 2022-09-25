package org.lizhao.base.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;

/**
 * Description 用户组实体 用户集合
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:26
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@TableName("user_group")
public class UserGroup extends AppendInfo {

    /**
     * 用户组Id
     */
    @TableId
    private String id;

    /**
     * 用户组名称
     */
    private String name;

    /**
     * 用户组状态：0-初始;1-可用
     */
    private Integer status;


}
