package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.AppendInfo;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.entity.user.UserGroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Description 用户实体{@link User}和用户组实体{@link UserGroup}的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:46
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
//@TableName("user_user_group_relation")
public class UserUserGroupRelation extends AppendInfo {

    /**
     * 用户{@link User}和用户组 {@link UserGroup} 关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 用户Id {@link User}
     */
    @Column
    private String userId;

    /**
     * 用户组Id {@link UserGroup}
     */
    @Column
    private String userGroupId;

    /**
     * 关系是否有效：true-起效；false|null-无效
     */
    @Column
    private Boolean valid;
}
