package org.lizhao.base.enums;

import lombok.Getter;

/**
 * Description 权限枚举类 {@link org.lizhao.base.entity.authority.Authority}
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-25 0:58
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public enum AuthorityEnum {

    /**
     * 根权限， 万能权限
     */
    ROOT("root");

    final String code;

    AuthorityEnum(String code) {
        this.code = code;
    }

}
