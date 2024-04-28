package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-25 0:31
 * @since jdk-1.8.0
 */
@Getter
public enum RoleEnum {

    /**
     * root 角色
     */
    ROOT("root"),
    /**
     * admin 角色
     */
    ADMIN("admin");

    final String code;

    RoleEnum(String name) {
        this.code = name.toLowerCase();
    }



}
