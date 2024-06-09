package org.lizhao.realtime.srs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description srs 响应体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 15:38
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class SrsResponse {

    private Integer code;

    private String pid;

    /**
     * 用户标识srs服务器,变化则表明服务器重启了
     */
    private String server;

    private String service;

    private Vhost[] vhosts;

    private Stream[] streams;

    private Client[] clients;
}
