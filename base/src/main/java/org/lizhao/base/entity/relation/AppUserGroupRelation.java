package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.App;
import org.lizhao.base.entity.user.UserGroup;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "app_user_group_relation")
public class AppUserGroupRelation {

    /**
     * {@link App} 和 {@link UserGroup} 的关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 应用Id {@link App#getId()}
     */
    @Column
    private String appId;

    /**
     * 用户组Id {@link UserGroup#getId()}
     */
    @Column
    private String userGroupId;

    /**
     * 关系是否有效：true-有效;false|null-无效
     */
    @Column
    private Boolean valid;

}
