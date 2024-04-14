package org.lizhao.cloud.web.react.json.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lizhao.cloud.gateway.utils.json.deserializer.PredicateDefinitionDeserializer;
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
import java.util.Map;

/**
 * Description 路由决策定义 解码器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-01 17:46
 * @since 0.0.1-SNAPSHOT
 */
public class PredicateDefinitionDecoder extends AbstractDecoder<PredicateDefinition> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Flux<PredicateDefinition> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        Flux<DataBuffer> dataBufferFlux = Flux.from(inputStream);
        return Flux.deferContextual(contextView ->
            dataBufferFlux.handle((dataBuffer, sink) -> {
                try {
                    JsonNode node = mapper.readTree(dataBuffer.asInputStream());
                    sink.next(PredicateDefinitionDeserializer.parse(mapper, node));
                } catch (IOException e) {
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
