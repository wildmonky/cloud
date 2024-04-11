package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
@Table(name = "role_authority_relation")
public class RoleAuthorityRelation extends Relation {

    /**
     * 权限{@link Authority}和角色{@link Role}的关系Id
     */
    @Id
    private String id;

    /**
     * 角色Id {@link Role#getId()}
     */
    @Column
    private String roleId;

    /**
     * 权限Id {@link Authority#getId()}
     */
    @Column
    private String authorityId;

}
