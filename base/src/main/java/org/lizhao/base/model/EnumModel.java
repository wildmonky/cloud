package org.lizhao.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 枚举类 通用model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-25 23:39
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class EnumModel<T> {

    private String name;

    private T code;

    private String details;

}
