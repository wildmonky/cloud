package org.lizhao.cloud.gateway.handler;

import org.apache.http.entity.ContentType;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Description 全局响应封装
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-08-14 19:09
 * @since 0.0.1-SNAPSHOT
 */
public class GlobalResponseBodyHandler extends ResponseBodyResultHandler {

    private static MethodParameter GLOBAL_METHOD_PARAMETER;

    static {
        try {
            GLOBAL_METHOD_PARAMETER = new MethodParameter(GlobalResponseBodyHandler.class.getDeclaredMethod("methodParameter"), -1);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public GlobalResponseBodyHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    public GlobalResponseBodyHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver, ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }

    @Override
    public boolean supports(HandlerResult result) {
        MethodParameter returnType = result.getReturnTypeSource();
        Class<?> containingClass = returnType.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object body = result.getReturnValue();
//        MethodParameter bodyTypeParameter = result.getReturnTypeSource();
        if (body instanceof Mono) {
            body = ((Mono<Object>) body)
                    .map(this::wrapResponseBody)
                    .defaultIfEmpty(ResponseBodyModel.success(null));
        }else if (body instanceof Flux) {
            body = ((Flux<Object>) body)
                    .collectList()
                    .map(this::wrapResponseBody)
                    .defaultIfEmpty(ResponseBodyModel.success(null));
        }else {
            body = wrapResponseBody(result);
        }

        //设置响应类型，否则导致 No Encoder for [org.lizhao.base.model.ResponseBodyModel<?>] with preset Content-Type 'null'
        Optional.ofNullable(result.getReturnTypeSource().getMethodAnnotation(RequestMapping.class)).ifPresent(e -> {
            exchange.getResponse().getHeaders().addAll("Content-Type", Arrays.asList(e.consumes()));
        });

        return writeBody(body, GLOBAL_METHOD_PARAMETER, exchange);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseBodyModel<T> wrapResponseBody(T result) {
        if (result instanceof ResponseBodyModel) {
            return (ResponseBodyModel<T>) result;
        }else{
            if (result instanceof Exception) {
                return ResponseBodyModel.error(result);
            }
            return ResponseBodyModel.success(result);
        }
    }

    private static <T> Mono<ResponseBodyModel<T>> methodParameter() {
        return Mono.empty();
    }
}
