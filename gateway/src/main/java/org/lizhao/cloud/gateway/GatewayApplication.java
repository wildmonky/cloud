package org.lizhao.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author lizhao
 */
@ConfigurationPropertiesScan(basePackages = {"org.lizhao.cloud.gateway.configurer.properties"})
//@EnableKafka
@EnableAspectJAutoProxy
@EnableRedisRepositories
//@EnableReactiveMongoRepositories
@SpringBootApplication(scanBasePackages = {"org.lizhao"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
