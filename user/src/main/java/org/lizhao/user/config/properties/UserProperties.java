package org.lizhao.user.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-30 16:28
 * @since 0.0.1-SNAPSHOT
 */
@ConfigurationProperties(prefix = "user")
public class UserProperties {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
