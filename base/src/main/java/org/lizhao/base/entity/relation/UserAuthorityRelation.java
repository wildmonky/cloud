package org.lizhao.base.entity.relation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.user.User;

/**
 * Description 用户-权限关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-17 20:24
 * @since 0.0.1-SNAPSHOT
 */

@Getter
@Setter
@Table(name = "user_authority_relation")
public class UserAuthorityRelation {

    /**
     * 用户{@link User}和用户组 {@link Authority} 关系Id
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
     * 用户组Id {@link Authority}
     */
    @Column
    private String authorityId;

    /**
     * 关系是否有效：true-起效；false|null-无效
     */
    @Column
    private Boolean valid;

}
