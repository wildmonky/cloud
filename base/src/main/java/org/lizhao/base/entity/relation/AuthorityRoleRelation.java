package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description 权限实体{@link Authority}和角色实体{@link Role}的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:51
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "authority_role_relation")
public class AuthorityRoleRelation extends CommonAttribute {

    /**
     * 权限{@link Authority}和角色{@link Role}的关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 权限Id {@link Authority#getId()}
     */
    @Column
    private String authorityId;

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
