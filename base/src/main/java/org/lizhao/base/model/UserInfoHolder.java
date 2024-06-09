package org.lizhao.base.model;

import java.util.Optional;

/**
 * Description 用户信息持有者
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 15:03
 * @since 0.0.1-SNAPSHOT
 */
public class UserInfoHolder {

    private static final ThreadLocal<SimpleUserInfo> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(SimpleUserInfo userInfo) {
        USER_INFO_THREAD_LOCAL.set(userInfo);
    }

    public static SimpleUserInfo get() {
        return Optional.ofNullable(USER_INFO_THREAD_LOCAL.get()).orElseThrow(() -> new RuntimeException("用户信息不存在"));
    }

    public static void remove() {
        USER_INFO_THREAD_LOCAL.remove();
    }

}
