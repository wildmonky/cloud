package org.lizhao.base.enums.realtime;

import lombok.Getter;

/**
 * Description realtime 平台
 * srs
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-08 17:06
 * @since jdk-1.8.0
 */
@Getter
public enum PlatformEnum {

    SRS("srs", "https://ossrs.net/lts/zh-cn/");

    private final String platform;
    private final String url;

    PlatformEnum(String platform, String url) {
        this.platform = platform;
        this.url = url;
    }

}
