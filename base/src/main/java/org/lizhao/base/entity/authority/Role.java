package org.lizhao.base.entity.authority;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 角色名称
     */
    @Column
    private String name;

    /**
     * 角色状态：0-初始；1-可用
     */
    @Column
    private Integer status;
}
