package org.lizhao.base.enums;

/**
 * Description 资源状态枚举
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-24 16:52
 * @since jdk-1.8.0
 */
public enum ResourceStateEnum {

    /*
    资源已创建
    */
    CREATED(0, "创建"),
    /*
    资源被锁定
     */
    LOCKED(1,  "锁定"),
    /*
    资源已失效
    */
    INVALID(2, "失效");


    private final Integer code;
    private final String description;

    ResourceStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 检测 code 枚举元素是否匹配
     * @param code 状态码
     * @return true code与枚举元素的code相同；
     *         false  code与枚举元素的code不同
     */
    public boolean check(int code) {
        return this.code == code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
