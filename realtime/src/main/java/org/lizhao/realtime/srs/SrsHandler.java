package org.lizhao.realtime.srs;

import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.exception.MessageException;
import org.lizhao.realtime.configurer.properties.SrsProperties;
import org.lizhao.realtime.srs.model.SrsResponse;
import org.lizhao.realtime.srs.model.Stream;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Description srs handler
 * vhost--------
 *          |
 *          |--- stream--------
 *                          |
 *                          | 推流的client
 *                          |
 *                          | 播放流的client 1
 *                          | 播放流的client 2
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 14:48
 * @since 0.0.1-SNAPSHOT
 */
public class SrsHandler {

//    private final RestClient srsRestClient;
    private final RestTemplate srsRestTemplate;

    private final SrsProperties srsProperties;

    public SrsHandler(RestTemplate srsRestTemplate, SrsProperties srsProperties) {
        this.srsRestTemplate = srsRestTemplate;
        this.srsProperties = srsProperties;
    }

    public SrsResponse searchVHost(String vHostId) {
        return srsRestTemplate
                .exchange(srsProperties.getVHostApi() + "/" + vHostId, HttpMethod.GET, HttpEntity.EMPTY, SrsResponse.class)
                .getBody();
    }

    public SrsResponse allStream() {
        String url = srsProperties.getStreamApi();
        SrsResponse srs = srsRestTemplate
                .exchange(url, HttpMethod.GET, HttpEntity.EMPTY, SrsResponse.class)
                .getBody();

        if (srs == null) {
            throw new MessageException("{}响应为null", url);
        }
        return srs;
    }


    public SrsResponse searchStream(String streamId) {
        String url = srsProperties.getStreamApi() + "/" + streamId;
        SrsResponse srs = srsRestTemplate
                .exchange(url, HttpMethod.GET, HttpEntity.EMPTY, SrsResponse.class)
                .getBody();

        if (srs == null) {
            throw new MessageException("{}响应为null", url);
        }
        return srs;
    }

    /**
     * 结束流的推送
     * @param streamId 流id
     */
    public void streamDown(String streamId) {
        SrsResponse srsResponse = searchStream(streamId);
        Stream[] streams = srsResponse.getStreams();
        if (ObjectUtils.isEmpty(streams)) {
            throw new MessageException("{}对应的流不存在", streamId);
        }
        Stream stream = streams[0];
        Stream.Publish publish = stream.getPublish();
        if (publish == null) {
            throw new RuntimeException(String.format("流%s的推送客户端信息缺失", streamId));
        }

        kickOut(publish.getCid());
    }

    /**
     * 将客户端踢出
     * @param clientId 客户端id
     */
    public void kickOut(String clientId) {
        srsRestTemplate.delete(srsProperties.getClientApi() + "/" + clientId);
    }

}
