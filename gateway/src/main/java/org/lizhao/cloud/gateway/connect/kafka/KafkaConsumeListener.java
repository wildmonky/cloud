package org.lizhao.cloud.gateway.connect.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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

    @KafkaListener(topics = "gateway", id = "spring test consumer", groupId = "spring boot test")
    public void consumer(String content, Acknowledgment acknowledgment) {
        System.out.println(content);
        acknowledgment.acknowledge();
    }



}
