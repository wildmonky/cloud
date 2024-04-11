package org.lizhao.cloud.gateway.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 组角色模型
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 17:01
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class GroupRoleModel {

    private String groupId;

    private String groupName;
    /**
     * 0-初始;1-可用;2-禁用
     */
    private Integer groupStatus;

    private String relationId;

    private String roleId;

    private String roleName;
    /**
     * 0-初始;1-可用;2-禁用
     */
    private Integer roleStatus;
    /**
     * 备注
     */
    private String roleComment;
}
