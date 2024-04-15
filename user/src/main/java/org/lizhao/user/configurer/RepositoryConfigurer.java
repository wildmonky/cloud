package org.lizhao.user.configurer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import reactor.core.publisher.Mono;

/**
 * Description Repository 配置类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-18 23:11
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories("org.lizhao.user.repository")
public class RepositoryConfigurer {

    /**
     * 相当于实现ReactiveAuditorAware，这里是给@CreatedBy赋值的
     * <p>创建时间和修改时间，r2dbc底层会自动去判断并生成</p>
     */
    @Bean
    ReactiveAuditorAware<String> auditorAware() {
        return () -> Mono.just("system");
    }

    /**
     * relationalMappingContext.setForceQuote(true);
     * 为所有表名设置 quota，避免 user、group等关键字影响
     *
     * @param namingStrategy
     * @param r2dbcCustomConversions
     * @param r2dbcManagedTypes
     * @return
     */
    @Bean
    public R2dbcMappingContext r2dbcMappingContext(ObjectProvider<NamingStrategy> namingStrategy,
                                                   R2dbcCustomConversions r2dbcCustomConversions,
                                                   RelationalManagedTypes r2dbcManagedTypes) {
        R2dbcMappingContext relationalMappingContext = new R2dbcMappingContext(
                namingStrategy.getIfAvailable(() -> DefaultNamingStrategy.INSTANCE));
        relationalMappingContext.setSimpleTypeHolder(r2dbcCustomConversions.getSimpleTypeHolder());
        relationalMappingContext.setManagedTypes(r2dbcManagedTypes);
        relationalMappingContext.setForceQuote(true);
        return relationalMappingContext;
    }

}
