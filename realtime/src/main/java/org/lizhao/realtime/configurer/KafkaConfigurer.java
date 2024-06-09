package org.lizhao.realtime.configurer;

import org.springframework.context.annotation.Configuration;

/**
 * Description KafkaConfigurer
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-13 14:19
 * @since 0.0.1-SNAPSHOT
 */
//@EnableKafka
@Configuration
public class KafkaConfigurer {

    /**
     * @return NewTopic
     * @see KafkaAdmin#createOrModifyTopics addOrModifyTopicsIfNeeded method
     */
//    @Bean
//    public NewTopic userTopic() {
//        return TopicBuilder.name("user")
//                .partitions(1)
//                .replicas(1)
//                .compact()
//                .build();
//    }

}
