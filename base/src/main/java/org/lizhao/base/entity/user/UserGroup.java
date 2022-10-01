package org.lizhao.base.entity.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.AppendInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table
public class UserGroup extends AppendInfo {

    /**
     * 用户组Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 用户组名称
     */
    @Column
    private String name;

    /**
     * 用户组状态：0-初始;1-可用
     */
    @Column
    private Integer status;


}
