package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.App;
import org.lizhao.base.entity.user.UserGroup;

/**
 * Description 应用实体{@link App} 和 用户组实体{@link UserGroup} 的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:36
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class AppUserGroupRelation {

    /**
     * {@link App} 和 {@link UserGroup} 的关系Id
     */
    private String id;

    /**
     * 应用Id {@link App#getId()}
     */
    private String appId;

    /**
     * 用户组Id {@link UserGroup#getId()}
     */
    private String userGroupId;

    /**
     * 关系是否有效：true-有效;false|null-无效
     */
    private Boolean valid;

}
