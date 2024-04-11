package org.lizhao.base.entity.authority;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

/**
 * Description 权限实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:41
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "authority")
public class Authority extends CommonAttribute {

    /**
     * 权限Id
     */
    @Id
    private String id;
    /**
     * 权限名称
     */
    @Column
    private String name;
    /**
     * 权限状态：0-初始；1-可用; 2-停用
     */
    @Column
    private Integer status;
    /**
     * 备注
     */
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        if (!Objects.equals(id, authority.id)) {
            return false;
        }
        if (!Objects.equals(name, authority.name)) {
            return false;
        }
        if (!Objects.equals(status, authority.status)) {
            return false;
        }
        return Objects.equals(comment, authority.comment);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
