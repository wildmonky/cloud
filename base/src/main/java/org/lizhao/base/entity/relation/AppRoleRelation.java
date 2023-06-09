package org.lizhao.base.entity.relation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.App;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.authority.Role;

/**
 * Description 应用实体{@link App} 和 角色实体{@link Role} 的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 17:31
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "app_role_relation")
public class AppRoleRelation extends CommonAttribute {

    /**
     * 应用实体{@link App} 和 角色实体{@link Role} 的关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
    private String id;

    /**
     * 应用Id {@link App#getId()}
     */
    @Column
    private String appId;

    /**
     * 角色Id {@link Role#getId()}
     */
    @Column
    private String roleId;

    /**
     * 关系是否有效：true-有效；false|null-无效
     */
    @Column
    private Boolean valid;
}
