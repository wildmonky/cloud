package org.lizhao.realtime.srs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description srs stream Model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 15:10
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class Stream {

    /**
     * 流stream id
     */
    private String id;

    /**
     * stream name流名称
     */
    private String name;

    /**
     * webrtc://host/[app]/[name] 中的app
     * 可作为房间号
     */
    private String app;
    /**
     * webrtc url
     * pattern: webrtc://host/[app]/[name]
     */
    private String tcUrl;

    /**
     * pattern: /[app]/[name]
     */
    private String url;

    /**
     * stream 所属的 vHost
     */
    private String vHost;
    /**
     * 使用流的客户端数量，如推流客户端和播放客户端
     */
    private Integer clients;

    /**
     * 推流客户端信息
     */
    private Publish publish;

    /**
     * stream live time 流存活时间
     */
    private Long liveMs;

    /**
     * 接收字节数，流量统计
     */
    private Long recvBytes;

    /**
     * 发送字节数，流量统计
     */
    private Long sendBytes;

    @Getter
    @Setter
    public static class Publish{
        /**
         * 推流客户端是否存活
         */
        private Boolean active;
        /**
         * 推流客户端id
         */
        private String cid;
    }

}
