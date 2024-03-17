package org.lizhao.base.entity.authority;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.springframework.security.core.GrantedAuthority;

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
public class Authority extends CommonAttribute implements GrantedAuthority {

    /**
     * 权限Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
    private String id;

    /*
     * 父级权限Id
     */
    private String parentId;

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
    public String getAuthority() {
        return null;
    }
}
