package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.cloud.gateway.filter.httpHeader.AddUserInfoRequestHeaderGlobalFilter;
import org.lizhao.cloud.gateway.json.definition.RouteDefinitionDeserializer;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description 网关配置类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 13:30
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class GatewayConfigurer  {

//    /**
//     * Fluent Java API 定义路由
//     *
//     * @param builder
//     * @return 路由定位
//     */
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes().route("test", r -> r
//                .path("/gateway/**")
//                .uri("https://www.baidu.com")
//        ).build();
//    }

    @Bean
    @ConditionalOnEnabledFilter
    public AddUserInfoRequestHeaderGlobalFilter addUserInfoRequestHeaderGlobalFilter(ObjectMapper objectMapper) {
        return new AddUserInfoRequestHeaderGlobalFilter(objectMapper);
    }
    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(0L, 1L, 0L);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

//                JavaTimeModule javaTimeModule = new JavaTimeModule();
        // yyyy-MM-dd HH:mm:ss LocalDateTime 序列化 反序列化
        SimpleModule simpleModule = new SimpleModule();

        final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        simpleModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String text = parser.getText();
                if (StringUtils.isNotBlank(text)) {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(text.trim(), dateTimeFormat);
                    return zonedDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }
                return null;
            }
        });
        simpleModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider)
                    throws IOException
            {
                ZonedDateTime zonedDateTime = value.atZone(ZoneId.systemDefault());
                g.writeString(zonedDateTime.format(dateTimeFormat));
            }
        });

        simpleModule.addDeserializer(RouteDefinition.class, new RouteDefinitionDeserializer());

        objectMapper.registerModules(simpleModule);
        return objectMapper;
    }

}
