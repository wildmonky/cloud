package org.lizhao.cloud.web.react.handler;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
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
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
//@ControllerAdvice(annotations = { Controller.class, RestController.class })
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
    public Mono<ResponseBodyModel<?>> exceptionHandler(Exception e) {
        log.error("捕获到异常{}, {}", e.getMessage(), e.getStackTrace());
        return Mono.just(ResponseBodyModel.error(e.getMessage()));
    }

}
