package org.lizhao.realtime.srs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description srs callback 传递的数据 Model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 16:35
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class SrsCallbackParam {

    /**
     *
     */
    private String action;

    /**
     * 客户端id,可用于踢出
     */
    private String clientId;

    /**
     * 客户端ip
     */
    private String ip;

    /**
     * vhost
     */
    private String vhost;

    /**
     * webrtc://host/[app]/[stream]
     */
    private String app;

    /**
     * stream id
     * webrtc://host/[app]/[stream]
     */
    private String stream;

    /**
     * 回调携带的参数，调用stream api的url上拼接的参数
     * 可用于验证
     * 例如：?token=xxxx&hhh=sss
     */
    private String param;

    /**
     * 只在ON_PLAY时有值，表示播放流的页面地址
     */
    private String pageUrl;

    /**
     * {@link SrsResponse#getServer()}
     */
    private String serverId;

}
