package org.lizhao.user.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Slf4j
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
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object body = result.getReturnValue();
        if (body instanceof Mono) {
            body = ((Mono<?>) body)
                    .map(o -> wrapResponseBody(o, exchange))
                    .defaultIfEmpty(ResponseBodyModel.success(null));
        }else if (body instanceof Flux) {
            body = ((Flux<?>) body)
                    .collectList()
                    .map(o -> wrapResponseBody(o, exchange))
                    .defaultIfEmpty(ResponseBodyModel.success(null));
        }else {
            body = wrapResponseBody(result, exchange);
        }

        //设置响应类型，否则导致 No Encoder for [org.lizhao.base.model.ResponseBodyModel<?>] with preset Content-Type 'null'
        Optional.ofNullable(result.getReturnTypeSource().getMethodAnnotation(RequestMapping.class)).map(RequestMapping::produces).ifPresent(l -> {
            try {
                exchange.getResponse().getHeaders().addAll("Content-Type", Arrays.asList(l));
            } catch(UnsupportedOperationException e) {
                log.info("ReadOnlyHeaders can not add");
            }
        });

        return writeBody(body, GLOBAL_METHOD_PARAMETER, exchange);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseBodyModel<T> wrapResponseBody(T result, ServerWebExchange exchange) {
        if (result instanceof ResponseBodyModel) {
            return (ResponseBodyModel<T>) result;
        }else{
            ResponseBodyModel<T> model = result instanceof Exception ? ResponseBodyModel.error(result) : ResponseBodyModel.success(result);
//            CsrfToken csrfToken = exchange.getAttribute("xorCsrfToken");
//            model.withCsrf(csrfToken == null ? "" : csrfToken.getToken());
            return model;
        }
    }

    private static <T> Mono<ResponseBodyModel<T>> methodParameter() {
        return Mono.empty();
    }
}
