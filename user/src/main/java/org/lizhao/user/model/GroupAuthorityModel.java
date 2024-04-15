package org.lizhao.user.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 21:51
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class GroupAuthorityModel {

    // 组
    private String groupId;

    private String groupName;
    /**
     * 0-初始;1-可用;2-禁用
     */
    private Integer groupStatus;
    /**
     * 备注
     */
    private String groupComment;

    private String relationId;

    private String authorityId;

    private String authorityName;

    private String authorityComment;

}
