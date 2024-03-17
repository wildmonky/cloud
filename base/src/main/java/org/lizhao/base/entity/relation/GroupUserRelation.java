package org.lizhao.base.entity.relation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.entity.user.Group;

/**
 * Description 用户实体{@link User}和用户组实体{@link Group}的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:46
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "group_user_relation")
public class GroupUserRelation extends CommonAttribute {

    /**
     * 用户{@link User}和用户组 {@link Group} 关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
    private String id;

    /**
     * 用户Id {@link User}
     */
    @Column
    private String userId;

    /**
     * 用户组Id {@link Group}
     */
    @Column
    private String userGroupId;

    /**
     * 关系是否有效：true-起效；false|null-无效
     */
    @Column
    private Boolean valid;
}
