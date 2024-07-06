package org.lizhao.realtime.srs.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * Description srs回调类型
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-05-08 16:39
 * @since jdk-1.8.0
 */
@Getter
public enum SrsEventEnum {

    ON_PUBLISH("on_publish", "publish", "推流时回调"),
    ON_PLAY("on_play", "play","播放时回调"),
    ON_STOP("on_stop", "stop","停止播放时回调"),
    ON_UNPUBLISH("on_unpublish", "unpublish","停止推流时回调");

    private final String name;
    private final String action;
    private final String description;

    SrsEventEnum(String name, String action, String description) {
        this.name = name;
        this.action = action;
        this.description = description;
    }

    public static SrsEventEnum of(String callback) {
        return Arrays.stream(values()).filter(e -> e.getName().equals(callback))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("没有%s对应的srs回调类型", callback)));
    }

}
