package org.lizhao.base.entity.user;

import lombok.Getter;
import lombok.Setter;

import org.lizhao.base.entity.AppendInfo;

import java.time.LocalDateTime;

/**
 * Description 人员信息实体类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 16:04
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class Person extends AppendInfo {

    /**
     * 人员信息主键
     */
    private String id;

    /**
     * 人员对应的用户账号Id {@link User#getId()}
     */
    private String userId;

    /**
     * 人员名称（真实姓名）
     */
    private String name;

    /**
     * 性别
     */
    private Boolean sex;

    /**
     * 年龄
     */
    private Byte age;

    /**
     * 出生日期
     */
    private LocalDateTime birthday;

    /**
     * 手机号码
     */
    private String mobilePhone;

}
