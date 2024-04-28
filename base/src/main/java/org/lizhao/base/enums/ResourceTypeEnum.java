package org.lizhao.base.enums;

import io.micrometer.common.util.StringUtils;
import org.lizhao.base.model.EnumModel;

/**
 * Description 资源类型枚举类
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-24 16:59
 * @since jdk-1.8.0
 */
public enum ResourceTypeEnum {

    /*
     项目
     */
    PROJECT(0, "project","项目"),
    /*
    路径 项目请求路径
    */
    PATH(1, "api","接口"),
    /*
    文件
     */
    FILE(2,  "file","文件");


    private final Integer code;

    private final String name;
    private final String description;

    ResourceTypeEnum(Integer code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static ResourceTypeEnum of(int code) {
        for (ResourceTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new RuntimeException(String.format("不存在 %s 对应的资源类型", code));
    }

    public static ResourceTypeEnum of(String name) {
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("资源类型名称不能为空");
        }
        for (ResourceTypeEnum value : values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new RuntimeException(String.format("不存在 %s 对应的资源类型", name));
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public EnumModel<Integer> model() {
        EnumModel<Integer> model = new EnumModel<>();
        model.setName(this.name);
        model.setCode(this.code);
        model.setDetails(this.description);
        return model;
    }

}
