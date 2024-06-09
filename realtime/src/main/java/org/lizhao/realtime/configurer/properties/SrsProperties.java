package org.lizhao.realtime.configurer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description
 * webrtc stream
 * ｛
 *  play: {
 *      active: true,
 *      cid: playClientId
 *  },
 *  publish: {
 *      active: true,
 *      cid: publishClientId
 *  }
 * ｝
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 0:33
 * @since 0.0.1-SNAPSHOT
 */
@ConfigurationProperties(prefix = "srs")
public class SrsProperties {

    private String url = "https://127.0.0.1:1990";

    /**
     * srs 默认api端口 http
     */
    private int httpPort = 1985;

    private int httpsPort = 1990;

    /**
     * vHostApi
     * GET - 查询所有的vhosts
     * vHostApi + “/” + vhostId
     * GET - 查询 vHostId 对应的 vHost信息；
     */
    private String vHostApi = "/api/v1/vhosts";

    /**
     * streamApi
     * GET - 查询 stream 信息，包含客户端信息。不加参数，默认前10条;
     * streamApi + "/" + streamId
     * GET - 查询streamId对应的 stream 信息，包含客户端;
     * 可选参数
     * ?start=N , N 默认是0
     * ?count=N, N 默认是10
     */
    private String streamApi = "/api/v1/streams";

    /**
     * clientApi
     * GET - 查询client详情，不传参数默认前10条
     * clientApi + "/" + clientId
     * GET - 查询clientId对应的client详情
     * DELETE - 踢掉客户端（如：推流的客户端，播放的客户端）
     * 可选参数
     * ?start=N , N 默认是0
     * ?count=N, N 默认是10
     */
    private String clientApi = "/api/v1/clients";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getVHostApi() {
        return vHostApi;
    }

    public void setVHostApi(String vHostApi) {
        this.vHostApi = vHostApi;
    }

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
    }

    public String getStreamApi() {
        return streamApi;
    }

    public void setStreamApi(String streamApi) {
        this.streamApi = streamApi;
    }

    public String getClientApi() {
        return clientApi;
    }

    public void setClientApi(String clientApi) {
        this.clientApi = clientApi;
    }

}
