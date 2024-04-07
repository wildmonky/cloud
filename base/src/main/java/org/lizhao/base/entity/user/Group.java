package org.lizhao.base.entity.user;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Collection;
import java.util.Objects;

/**
 * Description 用户组实体 用户集合
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:26
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "group")
public class Group extends CommonAttribute {

    /**
     * 用户组Id
     */
    @Id
    private String id;

    /**
     * 父级组Id
     */
    private String parentId;

    /**
     * 用户组名称
     */
    private String name;

    /**
     * 用户组状态：0-初始;1-可用;2-禁用
     */
    @InsertOnlyProperty
    private Integer status;

    @Transient
    private Collection<Group> child;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Group group = (Group) o;

        if (!Objects.equals(id, group.id)) {
            return false;
        }
        if (!Objects.equals(parentId, group.parentId)) {
            return false;
        }
        if (!Objects.equals(name, group.name)) {
            return false;
        }
        if (!Objects.equals(status, group.status)) {
            return false;
        }
        return Objects.equals(child, group.child);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }
}
