package org.lizhao.base.entity.user;

import lombok.*;
import org.lizhao.base.entity.CommonAttribute;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!id.equals(user.id)) {
            return false;
        }
        if (!phone.equals(user.phone)) {
            return false;
        }
        if (!name.equals(user.name)) {
            return false;
        }
        if (!password.equals(user.password)) {
            return false;
        }
        return status.equals(user.status);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
