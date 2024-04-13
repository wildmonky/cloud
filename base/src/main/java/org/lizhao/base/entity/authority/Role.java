package org.lizhao.base.entity.authority;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.model.TreeNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Collection;
import java.util.Objects;

/**
 * Description 角色实体 权限集合
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:45
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "role")
public class Role extends CommonAttribute implements TreeNode<Role> {

    /**
     * 角色Id
     */
    @Id
    private String id;

    /**
     * 父级角色Id
     */
    @Column
    private String parentId;

    /**
     * 角色名称
     */
    @Column
    private String name;

    /**
     * 角色状态：0-初始；1-可用；2-停用
     */
    @Column
    private Integer status;

    /**
     * 备注
     */
    private String comment;

    @Transient
    private Collection<TreeNode<Role>> children;

    @Override
    public Collection<TreeNode<Role>> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(Collection<TreeNode<Role>> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role role = (Role) o;

        if (!Objects.equals(id, role.id)) {
            return false;
        }
        if (!Objects.equals(parentId, role.parentId)) {
            return false;
        }
        if (!Objects.equals(name, role.name)) {
            return false;
        }
        if (!Objects.equals(status, role.status)) {
            return false;
        }
        if (!Objects.equals(comment, role.comment)) {
            return false;
        }
        return Objects.equals(children, role.children);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }
}
