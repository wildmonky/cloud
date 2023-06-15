package org.lizhao.cloud.gateway.configurer.nosql.mongo;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * description
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/9/17 16:04
 */
@Configuration
public class MongoConfigurer {

    @Resource
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory);
    }

}
