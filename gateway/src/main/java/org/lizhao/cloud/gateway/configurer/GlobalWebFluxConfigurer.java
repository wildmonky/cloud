package org.lizhao.cloud.gateway.configurer;

import jakarta.validation.constraints.NotNull;
import org.lizhao.base.web.react.handler.GlobalResponseBodyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

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

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.
//                addResourceHandler("/swagger-ui/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
//                .resourceChain(false);
//    }

}
