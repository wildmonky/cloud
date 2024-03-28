package org.lizhao.cloud.gateway.entity.authority;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
public class Role extends CommonAttribute {

    /**
     * 角色Id
     */
    @Id
    private String id;

    /*
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
}
