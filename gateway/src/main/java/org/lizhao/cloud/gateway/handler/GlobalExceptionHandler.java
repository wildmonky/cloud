package org.lizhao.cloud.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Description 全局异常处理
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-08-14 19:45
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@RestControllerAdvice(basePackages = {"org.lizhao.cloud.gateway.controller"})
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseBodyModel<?>> exceptionHandler(Exception e) {
        log.error("捕获到异常{}, {}", e.getMessage(), e.getStackTrace());
        return Mono.just(ResponseBodyModel.error(e.getMessage()));
    }

}
