package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import org.lizhao.cloud.gateway.json.deserializer.RouteDefinitionDeserializer;
import org.lizhao.cloud.web.react.handler.GlobalResponseBodyHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-10 16:14
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class MyWebFluxConfigurer implements WebFluxConfigurer {

    /**
     * 添加自定义类型转换 支持类型:
     *      1、Decoder, Encoder;
     *      2、HttpMessageReader, HttpMessageWriter;
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
                ObjectMapper objectMapper = abstractJackson2Decoder.getObjectMapper();

//                JavaTimeModule javaTimeModule = new JavaTimeModule();
                // yyyy-MM-dd HH:mm:ss LocalDateTime 序列化 反序列化
                SimpleModule simpleModule = new SimpleModule();

                simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                // route definition
//                simpleModule.addSerializer(RouteDefinition.class, new JsonSerializer<RouteDefinition>() {
//                    @Override
//                    public void serialize(RouteDefinition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                        gen.writeString(value.to);
//                    }
//                });
                simpleModule.addDeserializer(RouteDefinition.class, new RouteDefinitionDeserializer());

                objectMapper.registerModules(simpleModule);
            }
        });
//        configurer.customCodecs().register(new RouteDefinitionDecoder());
    }

    @Bean
    public GlobalResponseBodyHandler responseWrapper(@NotNull ServerCodecConfigurer serverCodecConfigurer,
                                                     RequestedContentTypeResolver requestedContentTypeResolver) {
        return new GlobalResponseBodyHandler(serverCodecConfigurer.getWriters(), requestedContentTypeResolver);
    }

}
