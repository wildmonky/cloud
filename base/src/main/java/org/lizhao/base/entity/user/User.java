package org.lizhao.base.entity.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description 用户实体类
 *
 * 系统用户实体信息：保存登录名和密码
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 15:55
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table
public class User extends CommonAttribute {

    /**
     * 用户账号主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 用户识别码，可以直接使用Id识别
     */
    @Column
    private String identity;

    /**
     * 用户登录账号
     */
    @Column
    private String name;

    /**
     * 用户登录密码
     */
    @Column
    private String password;

    /**
     * 用户状态: 0-初始；1-可用
     */
    @Column
    private Integer status;

}
