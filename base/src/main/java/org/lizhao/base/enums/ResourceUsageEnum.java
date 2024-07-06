package org.lizhao.base.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Description 资源用途
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-07-04 12:07
 * @since jdk-1.8.0
 */
@Getter
public enum ResourceUsageEnum {

    CALL(0,"call", "通话");

    private final int code;

    private final String usage;

    private final String name;

    ResourceUsageEnum(int code, String usage, String name) {
        this.code = code;
        this.name = name;
        this.usage = usage;
    }

    public static ResourceUsageEnum of(int code) {
        return Arrays.stream(values()).filter(e -> e.code == code).findFirst().orElseThrow(() -> new RuntimeException("没有code:" + code + "对应的资源使用途径"));
    }

    public static ResourceUsageEnum ofName(String usage) {
        return Arrays.stream(values()).filter(e -> StringUtils.equals(e.usage, usage)).findFirst().orElseThrow(() -> new RuntimeException("没有usage:" + usage + "对应的资源使用途径"));
    }

}
