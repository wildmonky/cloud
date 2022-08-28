package org.lizhao.base.entity.user;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.AppendInfo;

import java.math.BigInteger;

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
public class User extends AppendInfo {

    /**
     * 用户账号主键
     */
    private BigInteger id;

    /**
     * 用户识别码，可以直接使用Id识别
     */
    private String identity;

    /**
     * 用户登录账号
     */
    private String name;

    /**
     * 用户登录密码
     */
    private String password;

}
