package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.databind.*;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

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

    @Resource
    private ObjectMapper objectMapper;

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
        configurer.defaultCodecs().enableLoggingRequestDetails(true);
        configurer.defaultCodecs().configureDefaultCodec(codec -> {
            if (codec instanceof AbstractJackson2Decoder abstractJackson2Decoder) {
                abstractJackson2Decoder.setObjectMapper(objectMapper);
            }
        });
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
    }

}
