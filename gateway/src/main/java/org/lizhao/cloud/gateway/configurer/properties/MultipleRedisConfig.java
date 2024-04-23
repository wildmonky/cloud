package org.lizhao.cloud.gateway.configurer.properties;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-20 18:14
 * @since 0.0.1-SNAPSHOT
 */
@ConfigurationProperties(prefix = "data.multiple.redis")
public class MultipleRedisConfig {

    private Map<String, RedisProperties> multipleRedisMap;

    public Map<String, RedisProperties> getMultipleRedisMap() {
        return multipleRedisMap;
    }

    public void setMultipleRedisMap(Map<String, RedisProperties> multipleRedisMap) {
        this.multipleRedisMap = multipleRedisMap;
    }
}
