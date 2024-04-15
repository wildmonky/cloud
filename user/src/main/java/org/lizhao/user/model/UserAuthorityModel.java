package org.lizhao.user.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 21:43
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class UserAuthorityModel {

    private String userId;

    private String userName;

    private Integer userStatus;

    private String relationId;

    private String authorityId;

    private String authorityName;

    private String authorityComment;

}
