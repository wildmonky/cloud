package org.lizhao.user.configurer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-21 22:17
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class UserConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

//                JavaTimeModule javaTimeModule = new JavaTimeModule();
        // yyyy-MM-dd HH:mm:ss LocalDateTime 序列化 反序列化
        SimpleModule simpleModule = new SimpleModule();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        simpleModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {

            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                String text = p.getText();
                if (StringUtils.isNotBlank(text)) {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(text.trim(), dateTimeFormatter);
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
                g.writeString(zonedDateTime.format(dateTimeFormatter));
            }
        });

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

        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    @Bean
    public SnowFlake snowFlake(ObjectMapper objectMapper) {
        return new SnowFlake(0L, 2L, 0L);
    }

}
