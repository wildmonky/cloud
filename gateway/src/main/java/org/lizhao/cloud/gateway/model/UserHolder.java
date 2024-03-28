package org.lizhao.cloud.gateway.model;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Description 当前登录用户信息持有者
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-10-02 15:58
 * @since 0.0.1-SNAPSHOT
 */
public class UserHolder {

    private static final ThreadLocal<UserDetails> currentUser = new ThreadLocal<>();

    public static UserDetails getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(UserDetails user) {
        currentUser.set(user);
    }

    public static void removeCurrentUser() {
        currentUser.remove();
    }

}
