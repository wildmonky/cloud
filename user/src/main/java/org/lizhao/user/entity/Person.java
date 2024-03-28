package org.lizhao.user.entity;

import lombok.Getter;
import lombok.Setter;

import org.lizhao.base.entity.CommonAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
@Table(name = "person")
public class Person extends CommonAttribute {

    /**
     * 人员信息主键
     */
    @Id
    private String id;

    /**
     * 人员对应的用户账号Id
     */
    @Column
    private String userId;

    /**
     * 人员名称（真实姓名）
     */
    @Column
    private String name;

    /**
     * 性别
     */
    @Column
    private Boolean sex;

    /**
     * 年龄
     */
    @Column
    private Byte age;

    /**
     * 出生日期
     */
    @Column
    private LocalDateTime birthday;

    /**
     * 手机号码
     */
    @Column
    private String mobilePhone;

    /**
     * 座机号码
     */
    @Column
    private String telPhone;

    /**
     * 现居住地
     */
    @Column
    private String residence;

    /**
     * 出生地
     */
    @Column
    private String birthPlace;

    /**
     * 家乡
     */
    @Column
    private String homeTown;

    /**
     * 户口所在地
     */
    @Column
    private String censusRegister;

}
