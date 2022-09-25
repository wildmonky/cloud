package org.lizhao.cloud.gateway;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author lizhao
 */
@SpringBootApplication
@EnableWebFlux
@EnableAspectJAutoProxy
//@EnableRedisRepositories
//@EnableReactiveMongoRepositories
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
