package org.lizhao.web.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.lizhao.base.exception.MessageException;
import org.lizhao.base.model.ResponseBodyModel;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description 全路由 异常处理 MessageException异常信息写入响应
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-22 20:12
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class ReactiveMessageExceptionWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public ReactiveMessageExceptionWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof MessageException) {
            ServerHttpRequest request = exchange.getRequest();
            String rawQuery = request.getURI().getRawQuery();
            String query = StringUtils.hasText(rawQuery) ? "?" + rawQuery : "";
            HttpMethod httpMethod = request.getMethod();
            String description = "HTTP " + httpMethod + " \"" + request.getPath() + query + "\"";

            exchange.getResponse().getHeaders().add("content-type", "text/plain;charset=UTF-8");
            return exchange.getResponse().writeWith(bodyWrite -> {
                String message = ex.getMessage();
                ResponseBodyModel<String> responseBodyModel = ResponseBodyModel.error(message);
                DefaultDataBufferFactory sharedInstance = DefaultDataBufferFactory.sharedInstance;
                DefaultDataBuffer dataBuffer = null;
                try {
                    dataBuffer = sharedInstance.wrap(this.objectMapper.writeValueAsBytes(responseBodyModel));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                bodyWrite.onNext(dataBuffer);
                bodyWrite.onComplete();
            }).checkpoint(description + " [MessageExceptionWebExceptionHandler]");
        }
        return Mono.error(ex);
    }
}
