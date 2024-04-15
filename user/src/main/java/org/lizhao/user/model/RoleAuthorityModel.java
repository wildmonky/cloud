package org.lizhao.user.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 22:51
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class RoleAuthorityModel {

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

    private String relationId;

    private String authorityId;

    private String authorityName;

    private String authorityComment;

}
