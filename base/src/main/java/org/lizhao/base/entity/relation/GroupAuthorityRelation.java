package org.lizhao.base.entity.relation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.user.Group;

/**
 * Description 组和权限关系的实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-17 20:24
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "group_authority_relation")
public class GroupAuthorityRelation {

    /**
     * 用户组 {@link Group}和权限{@link Authority}关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
    private String id;

    /**
     * 用户Id {@link Group}
     */
    @Column
    private String groupId;

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
