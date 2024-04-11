package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
public class UserAuthorityRelation extends Relation {

    /**
     * 用户{@link User}和用户组 {@link Authority} 关系Id
     */
    @Id
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

}
