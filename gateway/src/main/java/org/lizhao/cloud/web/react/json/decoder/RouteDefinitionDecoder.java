package org.lizhao.cloud.web.react.json.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lizhao.cloud.gateway.utils.json.deserializer.FilterDefinitionDeserializer;
import org.lizhao.cloud.gateway.utils.json.deserializer.PredicateDefinitionDeserializer;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.AbstractDecoder;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Description 路由定义 解码器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-01 12:20
 * @since 0.0.1-SNAPSHOT
 */
public class RouteDefinitionDecoder extends AbstractDecoder<RouteDefinition> {

    private final ObjectMapper mapper = new ObjectMapper();

    public RouteDefinitionDecoder() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    public Flux<RouteDefinition> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Flux<DataBuffer> processed = Flux.from(inputStream);
        return Flux.deferContextual(contextView ->
            processed.handle((dataBuffer, sink) -> {
                try {
                    JsonNode node = mapper.readTree(dataBuffer.asInputStream());
                    if (node != null) {
                        if (node.isArray()) {
                            node.elements().forEachRemaining(e -> sink.next(parse(e)));
                        }
                        if (node.isObject()) {
                            sink.next(parse(node));
                        }
                    }
                }
                catch (IOException ex) {
                    sink.error(ex);
                }
            }
        ));
    }

    public Mono<RouteDefinition> decodeToMono(Publisher<DataBuffer> inputStream, ResolvableType elementType,
                                              @Nullable MimeType mimeType, @Nullable Map<String, Object> hints){

        return Mono.deferContextual(contextView -> {

            Map<String, Object> hintsToUse = contextView.isEmpty() ? hints :
                    Hints.merge(hints, ContextView.class.getName(), contextView);

            return DataBufferUtils.join(inputStream, 2048).flatMap(dataBuffer ->
                    Mono.justOrEmpty(decode(dataBuffer, elementType, mimeType, hintsToUse)));
        });
    }


    private RouteDefinition parse(JsonNode node) {
        if (!node.isObject()) {
            throw new RuntimeException();
        }

        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        RouteDefinition routeDefinition = new RouteDefinition();

        while(iterator.hasNext()) {
            Map.Entry<String, JsonNode> next = iterator.next();
            String fieldName = next.getKey();
            JsonNode nextNode = next.getValue();

            switch(fieldName) {
                case "id" -> routeDefinition.setId(nextNode.asText());
                case "order" -> routeDefinition.setOrder(nextNode.asInt(0));
                case "uri" -> routeDefinition.setUri(URI.create(nextNode.asText()));
                case "predicates" -> routeDefinition.setPredicates(parseDefinition(nextNode, PredicateDefinition.class));
                case "filters" -> routeDefinition.setFilters(parseDefinition(nextNode, FilterDefinition.class));
                default -> {}
            }

        }
        return routeDefinition;
    }

    private <T> List<T> parseDefinition(JsonNode jsonNode, Class<T> clazz) {
        if (!jsonNode.isArray()) {
            throw new RuntimeException();
        }
        List<T> re = new ArrayList<>();

        jsonNode.elements().forEachRemaining(child -> {
            Object o;
            if (clazz.equals(PredicateDefinition.class)) {
                o = parsePredicate(child);
            } else if(clazz.equals(FilterDefinition.class)) {
                o = parseFilter(child);
            } else {
                throw new RuntimeException("Definition类型异常");
            }
            re.add(clazz.cast(o));
        });
        return re;
    }

    private PredicateDefinition parsePredicate(JsonNode jsonNode) {
        return PredicateDefinitionDeserializer.parse(jsonNode);
    }

    private FilterDefinition parseFilter(JsonNode jsonNode) {
        return FilterDefinitionDeserializer.parse(jsonNode);
    }

}
