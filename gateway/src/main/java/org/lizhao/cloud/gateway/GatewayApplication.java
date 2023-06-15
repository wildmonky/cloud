package org.lizhao.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author lizhao
 */
@SpringBootApplication
@EnableWebFlux
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableMongoRepositories
@EnableRedisRepositories
@EnableReactiveMongoRepositories
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
