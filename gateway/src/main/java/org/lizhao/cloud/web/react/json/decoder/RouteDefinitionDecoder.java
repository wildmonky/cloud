package org.lizhao.cloud.web.react.json.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lizhao.base.utils.reflect.ReflectUtil;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.*;

/**
 * Description TODO
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

    private static final String PREDICATE_DEFINITION_PACKAGE = "org.lizhao.cloud.gateway.model.predicateDefinition";

    private static final String FILTER_DEFINITION_PACKAGE = "org.lizhao.cloud.gateway.model.filterDefinition";

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
        String field = ReflectUtil.upperFirstChar(jsonNode.get("name").asText());
        try {
            List<String> args = new ArrayList<>();
            switch (field) {
                case "After", "Before" -> args.add(jsonNode.get("time").asText());
                case "Between" -> args.addAll(jsonNode.findValuesAsText("timeList"));
                case "Header" -> {
                    args.add(jsonNode.get("headerName").asText());
                    args.add(jsonNode.get("regex").asText());
                }
                case "Host" -> args.addAll(jsonNode.findValuesAsText("hostPatternSet"));
                case "Method" -> args.addAll(jsonNode.findValuesAsText("httpMethodSet"));
                case "Path" -> args.addAll(jsonNode.findValuesAsText("pathMatcherSet"));
                case "Query" -> {
                    args.add(jsonNode.get("paramName").asText());
                    args.add(jsonNode.get("paramValue").asText());
                }
                case "RemoteAddr" -> args.add(jsonNode.get("remoteAddr").asText());
                case "XForwardedRemoteAddr" -> args.addAll(jsonNode.findValuesAsText("xForwardedRemoteAddrSet"));
                case "Weight" -> {
                    args.add(jsonNode.get("group").asText());
                    args.add(jsonNode.get("weight").asText());
                }
                default -> throw new RuntimeException(String.format("未知的%1$sPredicateDefinition", field));
            }
            List<? extends Class<?>> argsTypeList = args.stream().map(Object::getClass).toList();
            Class<?>[] argTypes = argsTypeList.toArray(new Class<?>[0]);
            Class<?> forName = Class.forName(PREDICATE_DEFINITION_PACKAGE + "." + field + "PredicateDefinition");
            Constructor<?>[] constructors = forName.getConstructors();
            Constructor<?> finalConstructor = Arrays.stream(constructors).filter(constructor -> {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (!parameterType.isAssignableFrom(argTypes[i])) {
                        return false;
                    }
                }
                return true;
            }).findFirst().orElseThrow(() -> new RuntimeException("未获取到对应的构造函数"));
            Object o = finalConstructor.newInstance(args.toArray(new Object[0]));
            return (PredicateDefinition) o;
        } catch (ClassNotFoundException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private FilterDefinition parseFilter(JsonNode jsonNode) {
        String field = ReflectUtil.upperFirstChar(jsonNode.get("name").asText());
        try {
            List<Object> args = new ArrayList<>();
            switch (field) {
                case "AddRequestHeader", "AddResponseHeader" -> {
                    args.add(jsonNode.get("headerName").asText());
                    args.add(jsonNode.get("headerValue").asText());
                }
                case "AddRequestHeadersIfNotPresent" -> {
                    JsonNode node = jsonNode.get("headerMap");
                    Map<String, String> map = new HashMap<>();
                    node.fields().forEachRemaining(ite -> {
                        map.put(ite.getKey(), ite.getValue().asText(""));
                    });
                    args.add(map);
                }
                case "AddRequestParameter" -> {
                    args.add(jsonNode.get("paramName").asText());
                    args.add(jsonNode.get("paramValue").asText());;
                }
                case "CircuitBreaker" -> {
                    args.add(jsonNode.get("circuitBreakerName").asText());
                    args.add(jsonNode.get("fallBackUri").asText());
                    JsonNode node = jsonNode.get("statusCodeSet");
                    Map<String, String> map = new HashMap<>();
                    node.fields().forEachRemaining(ite -> {
                        map.put(ite.getKey(), ite.getValue().asText(""));
                    });
                    args.add(map);
                }
                default -> throw new RuntimeException(String.format("未知的%1$sFilterDefinition", field));
            }
            List<? extends Class<?>> argsTypeList = args.stream().map(e -> e.getClass().getSuperclass()).toList();
            Class<?>[] argTypes = argsTypeList.toArray(new Class<?>[0]);
            Class<?> forName = Class.forName(FILTER_DEFINITION_PACKAGE + "." + field + "FilterDefinition");
            Constructor<?>[] constructors = forName.getConstructors();
            Constructor<?> finalConstructor = Arrays.stream(constructors).filter(constructor -> {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (!parameterType.isAssignableFrom(argTypes[i])) {
                        return false;
                    }
                }
                return true;
            }).findFirst().orElseThrow(() -> new RuntimeException("未获取到对应的构造函数"));
            Object o = finalConstructor.newInstance(args.toArray(new Object[0]));
            return (FilterDefinition) o;
        } catch (ClassNotFoundException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
