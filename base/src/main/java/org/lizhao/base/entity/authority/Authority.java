package org.lizhao.base.entity.authority;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.CommonAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
@Entity
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

}
