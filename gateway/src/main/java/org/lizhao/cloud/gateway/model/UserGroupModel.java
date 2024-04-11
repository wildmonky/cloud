package org.lizhao.cloud.gateway.model;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.user.Group;

import java.util.Collection;

/**
 * Description 用户组 Model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 9:59
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class UserGroupModel {

    private String userId;

    private String userName;

    private Integer userStatus;

    private String relationId;

    /**
     * true-起效；false-失效
     */
    private Boolean relationValid;

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

    private Collection<Group> childGroup;

}
