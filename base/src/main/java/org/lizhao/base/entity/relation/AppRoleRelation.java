package org.lizhao.base.entity.relation;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.App;
import org.lizhao.base.entity.AppendInfo;
import org.lizhao.base.entity.authority.Role;

/**
 * Description 应用实体{@link App} 和 角色实体{@link Role} 的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 17:31
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@TableName("app_role_relation")
public class AppRoleRelation extends AppendInfo {

    /**
     * 应用实体{@link App} 和 角色实体{@link Role} 的关系Id
     */
    @TableId
    private String id;

    /**
     * 应用Id {@link App#getId()}
     */
    private String appId;

    /**
     * 角色Id {@link Role#getId()}
     */
    private String roleId;

    /**
     * 关系是否有效：true-有效；false|null-无效
     */
    private Boolean valid;
}
