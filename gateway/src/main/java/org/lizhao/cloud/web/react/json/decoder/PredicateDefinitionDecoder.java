package org.lizhao.cloud.web.react.json.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lizhao.base.utils.reflect.ReflectUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.AbstractDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-01 17:46
 * @since 0.0.1-SNAPSHOT
 */
public class PredicateDefinitionDecoder extends AbstractDecoder<PredicateDefinition> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Flux<PredicateDefinition> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, MimeType mimeType, Map<String, Object> hints) {
        Flux<DataBuffer> dataBufferFlux = Flux.from(inputStream);
        return Flux.deferContextual(contextView ->
            dataBufferFlux.handle((dataBuffer, sink) -> {
                try {
                    JsonNode node = mapper.readTree(dataBuffer.asInputStream());
                    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
                    while (iterator.hasNext()) {
                        Map.Entry<String, JsonNode> next = iterator.next();
                        String field = ReflectUtil.upperFirstChar(next.getValue().asText());
                        JsonNode nextValue = next.getValue();
                        Class<?> forName = Class.forName(field + "PredicateDefinition");
                        Object o = new Object();
                        switch (field) {
                            case "After", "Before" ->
                                    o = forName.getDeclaredConstructor(String.class).newInstance(nextValue.asText());
                            case "Between" -> {
                                List<String> timeList = nextValue.findValuesAsText("timeList");
                                o = forName.getDeclaredConstructor(String.class, String.class).newInstance(timeList.get(0), timeList.get(1));
                            }
                            default -> {}
                        }
                        sink.next((PredicateDefinition) o);
                    }
                } catch (IOException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    sink.error(new RuntimeException(e));
                }
            })
        );
    }

    @Override
    public Mono<PredicateDefinition> decodeToMono(Publisher<DataBuffer> inputStream, ResolvableType elementType,
                                @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {

        throw new UnsupportedOperationException();
    }

}
