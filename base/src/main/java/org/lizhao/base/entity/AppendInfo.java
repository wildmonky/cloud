package org.lizhao.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.user.User;

import java.time.LocalDateTime;

/**
 * Description 追加信息
 * 默认所有实体都要继承
 *
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 16:17
 * @since 0.0.1-SNAPSHOT
 * @see org.lizhao.base.entity.user.User
 */
@Getter
@Setter
public class AppendInfo {

    /**
     * 记录创建时使用的账号的Id
     * {@link User#getId()}
     */
    private String createUserId;

    /**
     * 记录创建时使用的账号的用户名称 {@link User#getName()}
     */
    private String createUserName;

    /**
     * 记录创建时的时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时使用账号的Id {@link User#getId()}
     */
    private String updateUserId;

    /**
     * 记录更新时使用账号的用户名称 {@link User#getName()}
     */
    private String updateUserName;

    /**
     * 记录更新时的时间
     */
    private LocalDateTime updateTime;

}
