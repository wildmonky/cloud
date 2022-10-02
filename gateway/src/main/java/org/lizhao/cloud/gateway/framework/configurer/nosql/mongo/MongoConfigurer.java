package org.lizhao.cloud.gateway.framework.configurer.nosql.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * description
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/9/17 16:04
 */
//@Configuration
public class MongoConfigurer {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

}
