package org.lizhao.authority;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Description 配置类
 * 1、id生成器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-25 22:21
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class AuthorityConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

//                JavaTimeModule javaTimeModule = new JavaTimeModule();
        // yyyy-MM-dd HH:mm:ss LocalDateTime 序列化 反序列化
        SimpleModule simpleModule = new SimpleModule();

        DateTimeFormatter dateTimeOffsetFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        simpleModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {

            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                String text = p.getText();
                if (StringUtils.isNotBlank(text)) {
                    try {
                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(text.trim(), dateTimeOffsetFormatter);
                        return zonedDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    } catch (DateTimeParseException e) {
                        throw new RuntimeException(e);
                    }

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
                g.writeString(zonedDateTime.format(dateTimeOffsetFormatter));
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
    public SnowFlake idGenerator() {
        return new SnowFlake(1L, 2L, 1L);
    }

}
