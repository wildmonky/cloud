package org.lizhao.cloud.gateway.configurer.client.kafka;

import org.lizhao.cloud.gateway.security.context.repository.RedisSecurityContextRepository;
import org.springframework.kafka.support.Acknowledgment;

/**
 * Description Kafka consumer
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-16 16:35
 * @since 0.0.1-SNAPSHOT
 */
//@Component
public class KafkaConsumeListener {

//    @Resource
    private RedisSecurityContextRepository redisSecurityContextRepository;

    /**
     * 消费 密码 修改消息：
     *  清空redis存储的用户信息、token
     *
     * @param content
     * @param acknowledgment
     */
//    @KafkaListener(topics = "gateway",
//            id = "gateway-security",
//            groupId = "password-update"
//    )
    public void passwordUpdate(String content, Acknowledgment acknowledgment) {
        // 用户密码更新消息前缀
        String passwordUpdatePrefix = "password-update:";
        if (content.startsWith(passwordUpdatePrefix)) {
            String token = content.substring(content.indexOf(passwordUpdatePrefix));
            redisSecurityContextRepository.remove(token)
                    .doOnNext(f -> {
                        if (f) {
                            acknowledgment.acknowledge();
                        }
                    }).subscribe();
        }
    }

}
