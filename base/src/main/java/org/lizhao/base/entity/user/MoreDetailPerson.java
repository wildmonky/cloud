package org.lizhao.base.entity.user;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 更详细的人员信息实体
 * 一般使用 {@link Person} 就可以了
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 16:39
 * @since 0.0.1-SNAPSHOT
 * @see org.lizhao.base.entity.user.Person
 */
@Getter
@Setter
public class MoreDetailPerson extends Person{

    /**
     * 座机号码
     */
    private String telPhone;

    /**
     * 现居住地
     */
    private String residence;

    /**
     * 出生地
     */
    private String birthPlace;

    /**
     * 家乡
     */
    private String homeTown;

    /**
     * 户口所在地
     */
    private String censusRegister;

}
