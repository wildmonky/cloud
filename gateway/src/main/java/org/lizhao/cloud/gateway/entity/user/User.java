package org.lizhao.cloud.gateway.entity.user;

import lombok.*;
import org.lizhao.cloud.gateway.entity.CommonAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends CommonAttribute {

    /**
     * 用户账号主键，系统内唯一标识码
     */
    @Id
    private String id;

    /**
     * 用户手机号码，对外用户唯一识别码
     */
    @Column
    private String phone;

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
    @InsertOnlyProperty
    private Integer status;

}
