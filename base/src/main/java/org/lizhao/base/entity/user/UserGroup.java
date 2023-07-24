package org.lizhao.base.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;

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
@Entity
@Table(name = "user_group")
public class UserGroup extends CommonAttribute {

    /**
     * 用户组Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
    private String id;

    /**
     * 用户组名称
     */
    private String name;

    /**
     * 用户组状态：0-初始;1-可用;2-禁用
     */
    private Integer status;

}
