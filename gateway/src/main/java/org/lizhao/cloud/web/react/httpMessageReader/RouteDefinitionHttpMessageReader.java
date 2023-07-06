package org.lizhao.cloud.web.react.httpMessageReader;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.lang.Nullable;
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

    private final ResolvableType ROUTE_VALUE_TYPE = ResolvableType.forType(RouteDefinition.class);
    private final List<MediaType> MEDIA_TYPE_LIST = List.of(MediaType.APPLICATION_JSON);

    @Override
    public List<MediaType> getReadableMediaTypes() {
        return MEDIA_TYPE_LIST;
    }

    @Override
    public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
        if (!supportsMediaType(mediaType)) {
            return false;
        }
        if (RouteDefinition.class.isAssignableFrom(elementType.toClass()) && elementType.hasUnresolvableGenerics()) {
            return true;
        }
        return ROUTE_VALUE_TYPE.isAssignableFrom(elementType);
    }

    @Override
    public Flux<RouteDefinition> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return Flux.empty();
    }

    @Override
    public Mono<RouteDefinition> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return Mono.empty();
    }

    private boolean supportsMediaType(@Nullable MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }

        for (MediaType supportedMediaType : MEDIA_TYPE_LIST) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }
}
