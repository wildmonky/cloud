package org.lizhao.cloud.gateway.connect.kafka;

import jakarta.annotation.Resource;
import org.lizhao.cloud.gateway.security.userdetailsservice.RedisReactiveUserDetailsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Description Kafka consumer
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-16 16:35
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class KafkaConsumeListener {

    @Resource
    private RedisReactiveUserDetailsService redisReactiveUserDetailsService;

    /**
     * 消费 密码 修改消息：
     *  清空redis存储的用户信息、token
     *
     * @param content
     * @param acknowledgment
     */
    @KafkaListener(topics = "gateway",
            id = "gateway-security",
            groupId = "password-update"
    )
    public void passwordUpdate(String content, Acknowledgment acknowledgment) {
        // 用户密码更新消息前缀
        String passwordUpdatePrefix = "password-update:";
        if (content.startsWith(passwordUpdatePrefix)) {
            String username = content.substring(content.indexOf(passwordUpdatePrefix));
            redisReactiveUserDetailsService.remove(User.builder().username(username).build())
                    .doOnNext(f -> {
                        if (f) {
                            acknowledgment.acknowledge();
                        }
                    }).subscribe();
        }
    }

}
