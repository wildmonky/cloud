package org.lizhao.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 用户信息 在各微服务之间传递（request header）
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 14:56
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class UserInfo {

    private String id;

    private String name;

}
