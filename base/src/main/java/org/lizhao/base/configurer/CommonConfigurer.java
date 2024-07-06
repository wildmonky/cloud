package org.lizhao.base.configurer;

import org.lizhao.base.TransactionalHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Description 通用Bean配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-06-30 22:21
 * @since 0.0.1-SNAPSHOT
 */
@AutoConfiguration
@ComponentScan(basePackages = { "org.lizhao.base.utils" })
public class CommonConfigurer {

    @Bean
    public TransactionalHandler transactionalHandler() {
        return new TransactionalHandler();
    }

}
