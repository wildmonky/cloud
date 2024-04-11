package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Description 用户组-角色 关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-06 13:45
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "group_role_relation")
public class GroupRoleRelation extends CommonAttribute {


    /**
     * 用户组 {@link Group}和权限{@link Role}关系Id
     */
    @Id
    private String id;

    /**
     * 用户Id {@link Group}
     */
    @Column
    private String groupId;

    /**
     * 用户组Id {@link Role}
     */
    @Column
    private String roleId;

}
