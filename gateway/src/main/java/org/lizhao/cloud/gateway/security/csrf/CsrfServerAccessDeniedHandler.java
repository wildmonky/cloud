package org.lizhao.cloud.gateway.security.csrf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Description CSRF定义： 跨站请求伪造（英语：Cross-site request forgery)
 *              CSRF 访问拒绝处理
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-27 22:24
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class CsrfServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> Mono.just(exchange.getResponse())).flatMap((response) -> {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().set("content-type", "text/plain;charset=UTF-8");
            // 无效
//            response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
//            setAcceptCharset(Collections.singletonList(Charset.defaultCharset()));
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            DataBuffer buffer = dataBufferFactory.wrap("csrf检测异常".getBytes(StandardCharsets.UTF_8));
            log.info(denied.getMessage());
            return response.writeWith(Mono.just(buffer)).doOnError((error) -> DataBufferUtils.release(buffer));
        });
    }

}
