package org.lizhao.cloud.gateway.configurer.client.webClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Description 远程调用
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 19:35
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class WebClientConfigurer {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * 用户服务 远程客户端 设置了baseUrl
     * @param webClientBuilder WebClieny 构建器
     * @return webClient 链接 user service
     */
    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        return WebClientCreator.createDefaultWebClient(webClientBuilder.baseUrl("http://user-service/"), objectMapper);
    }

}
