package org.lizhao.realtime.configurer;

import org.lizhao.realtime.configurer.properties.SrsProperties;
import org.lizhao.realtime.srs.SrsCallbackHandler;
import org.lizhao.realtime.srs.SrsHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * Description realtime 配置类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 0:28
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class RealtimeConfigurer {

    @Bean
    public SrsHandler srsHandler(RestTemplate srsRestTemplate, SrsProperties srsProperties) {
        return new SrsHandler(srsRestTemplate, srsProperties);
    }
    /**
     * srs callback endpoint
     * @return RouterFunction<ServerResponse>
     */
    @Bean
    public RouterFunction<ServerResponse> serverResponseRouterFunction(SrsCallbackHandler srsCallbackHandler) {
        return route()
                .POST("/srs/callback/streams", accept(MediaType.APPLICATION_JSON), srsCallbackHandler::handleRequest)
                .build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate userRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://user-service")
                .build();
    }

}
