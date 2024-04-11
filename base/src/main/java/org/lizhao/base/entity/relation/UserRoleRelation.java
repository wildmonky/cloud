package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Description 用户和角色关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-17 20:29
 * @since 0.0.1-SNAPSHOT
 */

@Getter
@Setter
@Table(name = "user_role_relation")
public class UserRoleRelation implements Cloneable{

    /**
     * 用户{@link User}和用户组 {@link Role} 关系Id
     */
    @Id
    private String id;

    /**
     * 用户Id {@link User}
     */
    @Column
    private String userId;

    /**
     * 用户组Id {@link Role}
     */
    @Column
    private String roleId;

    @Override
    public UserRoleRelation clone() {
        try {
            UserRoleRelation clone = (UserRoleRelation) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
