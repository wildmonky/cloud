package org.lizhao.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableAspectJAutoProxy
@EnableRedisRepositories
//@EnableReactiveMongoRepositories
@SpringBootApplication(scanBasePackages = {"org.lizhao.cloud"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
