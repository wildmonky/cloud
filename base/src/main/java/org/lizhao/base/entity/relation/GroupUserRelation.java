package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
@Table(name = "group_user_relation")
public class GroupUserRelation extends CommonAttribute {

    /**
     * 用户{@link User}和用户组 {@link Group} 关系Id
     */
    @Id
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
    private String groupId;

}
