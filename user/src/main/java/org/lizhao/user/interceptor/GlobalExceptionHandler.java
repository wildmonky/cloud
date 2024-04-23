package org.lizhao.user.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.server.ServerResponse;
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
//@ControllerAdvice(annotations = { Controller.class })
public class GlobalExceptionHandler {

//    @ExceptionHandler(MessageException.class)
//    @ResponseBody
    public ResponseBodyModel<String> exceptionHandler(MessageException e) {
        log.error("捕获到异常{}, {}", e.getMessage(), e.getStackTrace());
        return ResponseBodyModel.error(e.getMessage());
    }

}
