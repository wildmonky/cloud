package org.lizhao.base.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.web.interceptor.GlobalExceptionHandler;
import org.lizhao.base.web.interceptor.ServletGlobalResponseBodyHandler;
import org.lizhao.base.web.interceptor.ServletRequestIntercept;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description web servlet mvc 配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-07 19:52
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 配置拦截器
     * @param registry 相当于拦截器的注册中心
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("web mvc 拦截器加载成功");
        registry.addInterceptor(new ServletRequestIntercept(objectMapper));
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public ServletGlobalResponseBodyHandler servletGlobalResponseBodyHandler() {
        return new ServletGlobalResponseBodyHandler();
    }

}
