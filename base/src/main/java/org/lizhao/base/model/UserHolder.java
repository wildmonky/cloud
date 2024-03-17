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
public class UserHolder {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static void removeCurrentUser() {
        currentUser.remove();
    }

}
