package org.lizhao.base.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.core.MethodParameter;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

/**
 * Description 全局响应封装
 * 增强 {@link org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler}
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-08-14 19:09
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Aspect
public class ReactiveGlobalResponseBodyHandler {

    private static MethodParameter GLOBAL_METHOD_PARAMETER;

    static {
        try {
            GLOBAL_METHOD_PARAMETER = new MethodParameter(ReactiveGlobalResponseBodyHandler.class.getDeclaredMethod("methodParameter"), -1);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private static <T> Mono<ResponseBodyModel<T>> methodParameter() {
        return Mono.empty();
    }


    @Around(value = "execution(* org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler.handleResult(..)) && args(exchange, result)", argNames = "pjp,exchange,result")
    public Mono<Void> handleResult(ProceedingJoinPoint pjp, ServerWebExchange exchange, HandlerResult result) throws Throwable {
        Object body = result.getReturnValue();
        MethodParameter methodParameter = result.getReturnTypeSource();
        if (body instanceof Mono) {
            body = ((Mono<?>) body)
                    .map(o -> wrapResponseBody(o, exchange))
                    .defaultIfEmpty(ResponseBodyModel.success(null));
            methodParameter = GLOBAL_METHOD_PARAMETER;
        }else if (body instanceof Flux) {
            body = ((Flux<?>) body)
                    .collectList()
                    .map(o -> wrapResponseBody(o, exchange))
                    .defaultIfEmpty(ResponseBodyModel.success(null));
            methodParameter = GLOBAL_METHOD_PARAMETER;
        }else if (!(body instanceof ProblemDetail)){
            body = wrapResponseBody(body, exchange);
            methodParameter = GLOBAL_METHOD_PARAMETER;
        }

        //设置响应类型，否则导致 No Encoder for [org.lizhao.base.model.ResponseBodyModel<?>] with preset Content-Type 'null'
        Optional.ofNullable(result.getReturnTypeSource().getMethodAnnotation(RequestMapping.class)).map(RequestMapping::produces).ifPresent(l -> {
            try {
                exchange.getResponse().getHeaders().addAll("Content-Type", Arrays.asList(l));
            } catch(UnsupportedOperationException e) {
                log.info("ReadOnlyHeaders can not add");
            }
        });
        HandlerResult newHandlerResult = new HandlerResult(result.getHandler(), body, methodParameter, result.getBindingContext());
        return (Mono<Void>) pjp.proceed(new Object[]{exchange, newHandlerResult});
    }

    private <T> ResponseBodyModel<T> wrapResponseBody(T result, ServerWebExchange exchange) {
        if (result instanceof ResponseBodyModel) {
            return (ResponseBodyModel<T>) result;
        }else{
            ResponseBodyModel<T> model = result instanceof Exception ? ResponseBodyModel.error(((Exception) result).getMessage()) : ResponseBodyModel.success(result);
//            CsrfToken csrfToken = exchange.getAttribute("xorCsrfToken");
//            model.withCsrf(csrfToken == null ? "" : csrfToken.getToken());
            return model;
        }
    }
}
