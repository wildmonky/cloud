package org.lizhao.base.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends CommonAttribute {

    /**
     * 用户账号主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", type = org.lizhao.database.jpa.IdentifierGeneratorImpl.class)
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
     * 用户状态: 0-初始; 1-可用; 2-禁用
     */
    @Column
    private Integer status;

    public static User of(String id, String identity, String name, String password, int status) {
        return new User(id, identity, name, password, status);
    }

}
