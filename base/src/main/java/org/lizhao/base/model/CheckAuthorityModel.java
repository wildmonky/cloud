package org.lizhao.base.model;


/**
 * Description 权限检查 model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-25 0:52
 * @since 0.0.1-SNAPSHOT
 */
public class CheckAuthorityModel {

    private String path;

    private String method;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
