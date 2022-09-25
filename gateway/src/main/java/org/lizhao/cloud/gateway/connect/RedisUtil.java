package org.lizhao.cloud.gateway.connect;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/8/29 19:13
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public void save() {



    }


}
