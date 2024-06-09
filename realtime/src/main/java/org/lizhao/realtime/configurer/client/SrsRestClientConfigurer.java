package org.lizhao.realtime.configurer.client;

import jakarta.annotation.Resource;
import org.lizhao.realtime.configurer.properties.SrsProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Description 远程调用
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 19:35
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class SrsRestClientConfigurer {

    @Resource
    private SrsProperties srsProperties;

//    @Bean
//    public RestClient.Builder restClientBuilder() {
//        return RestClient.builder();
//    }

//    /**
//     * srs服务 远程客户端 设置了baseUrl
//     * @param webClientBuilder WebClieny 构建器
//     * @return webClient 链接 user service
//     */
//    @Bean
//    public RestClient srsRestClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
//        return RestClientCreator.createDefaultRestClient(restClientBuilder.baseUrl(srsProperties.getUrl()), objectMapper);
//    }


    @Bean
    public RestTemplate srsRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(srsProperties.getUrl())
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .interceptors(new RestClientCreator.CustomClientHttpRequestInterceptor())
                .build();
    }


}
