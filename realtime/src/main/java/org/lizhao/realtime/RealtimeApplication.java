package org.lizhao.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ConfigurationPropertiesScan(basePackages = { "org.lizhao.realtime.configurer.properties" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "org.lizhao.realtime.repository" })
@EntityScan(basePackages = { "org.lizhao.base.entity.realtime" })
@EnableAsync
@SpringBootApplication
public class RealtimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealtimeApplication.class, args);
    }

}
