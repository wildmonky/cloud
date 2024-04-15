package org.lizhao.user.configurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.user.interceptor.GlobalResponseBodyHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-15 15:40
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class UserConfigurer implements WebFluxConfigurer {

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

                // json 数据格式 key为对象时
                simpleModule.addKeySerializer(User.class, new JsonSerializer<>() {
                    @Override
                    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeFieldName(objectMapper.writeValueAsString(value));
                    }
                });
                simpleModule.addKeyDeserializer(User.class, new KeyDeserializer() {
                    @Override
                    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                        return objectMapper.readValue(key, User.class);
                    }
                });

                simpleModule.addKeySerializer(Group.class, new JsonSerializer<>() {
                    @Override
                    public void serialize(Group value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeFieldName(objectMapper.writeValueAsString(value));
                    }
                });
                simpleModule.addKeyDeserializer(Group.class, new KeyDeserializer() {
                    @Override
                    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                        return objectMapper.readValue(key, Group.class);
                    }
                });

                simpleModule.addKeySerializer(Role.class, new JsonSerializer<>() {
                    @Override
                    public void serialize(Role value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeFieldName(objectMapper.writeValueAsString(value));
                    }
                });
                simpleModule.addKeyDeserializer(Role.class, new KeyDeserializer() {
                    @Override
                    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                        return objectMapper.readValue(key, Role.class);
                    }
                });

                simpleModule.addKeySerializer(Authority.class, new JsonSerializer<>() {
                    @Override
                    public void serialize(Authority value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeFieldName(objectMapper.writeValueAsString(value));
                    }
                });
                simpleModule.addKeyDeserializer(Authority.class, new KeyDeserializer() {
                    @Override
                    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                        return objectMapper.readValue(key, Authority.class);
                    }
                });

                objectMapper.registerModules(simpleModule);
            }
        });
    }

    @Bean
    public GlobalResponseBodyHandler responseWrapper(@NotNull ServerCodecConfigurer serverCodecConfigurer,
                                                     RequestedContentTypeResolver requestedContentTypeResolver) {
        return new GlobalResponseBodyHandler(serverCodecConfigurer.getWriters(), requestedContentTypeResolver);
    }

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(0L, 2L, 0L);
    }

}
