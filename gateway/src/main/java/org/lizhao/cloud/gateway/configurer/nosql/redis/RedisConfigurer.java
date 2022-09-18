package org.lizhao.cloud.gateway.configurer.nosql.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description TODO reids配置类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-04 18:00
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class RedisConfigurer {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> temp = new RedisTemplate<>();
        temp.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(new DefaultBaseTypeLimitingValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(om);

        //key,vlaue序列化方法
        temp.setKeySerializer(new StringRedisSerializer());
        temp.setValueSerializer(serializer);
        //hash时的序列化方法
        temp.setHashKeySerializer(new StringRedisSerializer());
        temp.setHashValueSerializer(serializer);
        temp.afterPropertiesSet();
        return temp;
    }

}
