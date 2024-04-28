package org.lizhao.base.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.entity.callback.CommonAttributeBeforeSaveConvert;
import org.lizhao.base.entity.callback.RelationBeforeSaveConvert;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.base.web.interceptor.ReactiveGlobalResponseBodyHandler;
import org.lizhao.base.web.interceptor.ReactiveMessageExceptionWebExceptionHandler;
import org.lizhao.base.web.interceptor.ReactiveRequestIntercept;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Description 全局拦截器、全局异常处理、请求拦截器 自动配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-22 21:59
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@EnableAspectJAutoProxy
@AutoConfiguration(before = { ErrorWebFluxAutoConfiguration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public ReactiveRequestIntercept reactiveRequestIntercept(ObjectMapper objectMapper) {
        return new ReactiveRequestIntercept(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = {"org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler"})
    public ReactiveGlobalResponseBodyHandler reactiveGlobalResponseBodyHandler() {
        log.info("响应体处理增强器已加载");
        return new ReactiveGlobalResponseBodyHandler();
    }

    /**
     * 在 {@link ErrorWebFluxAutoConfiguration} 前创建，否则会创建 {@link org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler}
     * 且排在前列，导致后续失效。
     * @param objectMapper json
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveMessageExceptionWebExceptionHandler reactiveMessageExceptionWebExceptionHandler(ObjectMapper objectMapper) {
        return new ReactiveMessageExceptionWebExceptionHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonAttributeBeforeSaveConvert commonAttributeBeforeSaveConvert(SnowFlake idGenerator) {
        return new CommonAttributeBeforeSaveConvert(idGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public RelationBeforeSaveConvert relationBeforeSaveConvert(SnowFlake idGenerator) {
        return new RelationBeforeSaveConvert(idGenerator);
    }

}
