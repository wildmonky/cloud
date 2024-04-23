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
public class SimpleUserInfo {

    private String id;

    private String name;

    private String[] groups;

    private String[] roles;

    private SimplePermission[] permissions;

    static class SimplePermission {

        String name;
        String comment;

        public SimplePermission(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

}
