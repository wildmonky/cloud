package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.lizhao.cloud.web.react.json.decoder.PredicateDefinitionDecoder;
import org.lizhao.cloud.web.react.json.decoder.RouteDefinitionDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.io.IOException;

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

    /**
     * 添加自定义类型转换 支持类型:
     *      1、Decoder, Encoder;
     *      2、HttpMessageReader, HttpMessageWriter;
     *      3、
     *
     * @see DefaultServerCodecConfigurer
     * org.springframework.http.codec.support.BaseCodecConfigurer的实现类
     * 从其getReaders()方法中可以看出自定义的优先级最高 {@link DefaultServerCodecConfigurer#getReaders()}
     * @param configurer the configurer to customize readers and writers
     */
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        //设置自定义request body 类型转换
//        configurer.customCodecs().register(new RouteDefinitionDecoder());
        configurer.defaultCodecs().enableLoggingRequestDetails(true);
        configurer.defaultCodecs().configureDefaultCodec(codec -> {
            if (codec instanceof AbstractJackson2Decoder abstractJackson2Decoder) {
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(PredicateDefinition.class, new JsonDeserializer<PredicateDefinition>() {
                    @Override
                    public PredicateDefinition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                        JsonToken jsonToken = p.nextToken();
                        while(jsonToken != null) {
                            jsonToken = p.nextToken();
                            System.out.println(jsonToken);
                        }
                        return null;
                    }
                });
                abstractJackson2Decoder.getObjectMapper().registerModule(simpleModule);
            }
        });
    }

}
