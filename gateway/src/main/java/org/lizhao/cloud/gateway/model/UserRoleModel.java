package org.lizhao.cloud.gateway.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 角色模型
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 16:56
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class UserRoleModel {

    private String userId;

    private String userName;
    /**
     * 0-初始;1-可用;2-禁用
     */
    private Integer userStatus;

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
