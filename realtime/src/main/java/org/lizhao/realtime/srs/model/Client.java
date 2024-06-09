package org.lizhao.realtime.srs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 15:11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class Client {

    /*
    客户端id
     */
    private String id;

    /*
    客户端名称
     */
    private String name;

    /*
    客户端对应的 stream id
     */
    private String stream;

    /*
    true-推流客户端;false-非推流客户端
     */
    private Boolean publish;

    /*
    客户端类型，如rtc-publish、rtc-play
     */
    private String type;

    /*
    rtcUrl，如：webrtc://host/room
     */
    private String tcUrl;

    /*
    /room/ + name(客户端名称)
     */
    private String url;

    /*
    客户端所属的vHost
     */
    private String vhost;

}
