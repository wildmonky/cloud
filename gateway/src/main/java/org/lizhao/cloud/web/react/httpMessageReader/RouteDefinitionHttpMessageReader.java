package org.lizhao.cloud.web.react.httpMessageReader;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-29 0:21
 * @since 0.0.1-SNAPSHOT
 */
public class RouteDefinitionHttpMessageReader extends LoggingCodecSupport
        implements HttpMessageReader<RouteDefinition> {

    @Override
    public List<MediaType> getReadableMediaTypes() {
        return null;
    }

    @Override
    public List<MediaType> getReadableMediaTypes(ResolvableType elementType) {
        return HttpMessageReader.super.getReadableMediaTypes(elementType);
    }

    @Override
    public boolean canRead(ResolvableType elementType, MediaType mediaType) {
        return false;
    }

    @Override
    public Flux<RouteDefinition> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return null;
    }

    @Override
    public Mono<RouteDefinition> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return null;
    }

    @Override
    public Flux<RouteDefinition> read(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
        return HttpMessageReader.super.read(actualType, elementType, request, response, hints);
    }

    @Override
    public Mono<RouteDefinition> readMono(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
        return HttpMessageReader.super.readMono(actualType, elementType, request, response, hints);
    }
}
