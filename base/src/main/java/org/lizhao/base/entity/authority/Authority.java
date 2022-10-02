package org.lizhao.base.entity.authority;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 权限名称
     */
    @Column
    private String name;

    /**
     * 权限状态：0-初始；1-可用
     */
    @Column
    private Integer status;
}
