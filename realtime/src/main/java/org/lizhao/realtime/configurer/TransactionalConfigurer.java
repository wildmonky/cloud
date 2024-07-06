package org.lizhao.realtime.configurer;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description Jpa 配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-07-06 18:32
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "org.lizhao.realtime.repository" })
@EntityScan(basePackages = { "org.lizhao.base.entity", "org.lizhao.realtime.srs.entity" })
public class TransactionalConfigurer {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

}
