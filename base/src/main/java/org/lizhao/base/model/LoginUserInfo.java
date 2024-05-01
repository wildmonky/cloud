package org.lizhao.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description 登录用户信息
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-30 23:17
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class LoginUserInfo extends SimpleUserInfo{

    private String token;

}
