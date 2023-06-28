package org.lizhao.cloud.gateway.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ResourceHttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;

/**
 * Description webflux全局配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-08-14 19:11
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class GlobalWebFluxConfigurer implements WebFluxConfigurer {

//    @Bean
//    public GlobalResponseBodyHandler responseWrapper(@NotNull ServerCodecConfigurer serverCodecConfigurer,
//                                                     RequestedContentTypeResolver requestedContentTypeResolver) {
//        return new GlobalResponseBodyHandler(serverCodecConfigurer.getWriters(), requestedContentTypeResolver);
//    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        //TODO 设置自定义 request body 类型转换
        configurer.getReaders().add(new ResourceHttpMessageReader());
        configurer.defaultCodecs().enableLoggingRequestDetails(true);
    }

}
