package org.lizhao.base.model;

import org.lizhao.base.entity.user.User;

/**
 * Description 当前登录用户信息持有者
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-10-02 15:58
 * @since 0.0.1-SNAPSHOT
 */
public class UserInfoHolder {

    private static User loginUser = null;

    public static User getCurrentUser() {
        return loginUser;
    }

    public static void setCurrentUser(User user) {
        loginUser = user;
    }

}
