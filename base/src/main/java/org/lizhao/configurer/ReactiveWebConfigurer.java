package org.lizhao.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lizhao.web.interceptor.ReactiveGlobalResponseBodyHandler;
import org.lizhao.web.interceptor.ReactiveMessageExceptionWebExceptionHandler;
import org.lizhao.web.interceptor.ReactiveRequestIntercept;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
@EnableAspectJAutoProxy
@AutoConfiguration(before = { ErrorWebFluxAutoConfiguration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebConfigurer {

    @Bean
    public ReactiveRequestIntercept reactiveRequestIntercept(ObjectMapper objectMapper) {
        return new ReactiveRequestIntercept(objectMapper);
    }

    @Bean
    public ReactiveGlobalResponseBodyHandler reactiveGlobalResponseBodyHandler() {
        return new ReactiveGlobalResponseBodyHandler();
    }

    /**
     * 在 {@link ErrorWebFluxAutoConfiguration} 前创建，否则会创建 {@link org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler}
     * 且排在前列，导致后续失效。
     * @param objectMapper json
     */
    @Bean
    public ReactiveMessageExceptionWebExceptionHandler reactiveMessageExceptionWebExceptionHandler(ObjectMapper objectMapper) {
        return new ReactiveMessageExceptionWebExceptionHandler(objectMapper);
    }

}
