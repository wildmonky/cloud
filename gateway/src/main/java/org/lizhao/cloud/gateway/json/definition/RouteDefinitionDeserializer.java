package org.lizhao.cloud.gateway.json.definition;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import jakarta.validation.constraints.NotNull;
import org.lizhao.base.utils.reflect.ReflectUtil;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.*;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-05 17:07
 * @since 0.0.1-SNAPSHOT
 */
public class RouteDefinitionDeserializer extends JsonDeserializer<RouteDefinition> {
    @Override
    public RouteDefinition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        RouteDefinition routeDefinition = new RouteDefinition();
        ObjectMapper codec = (ObjectMapper)p.getCodec();

        JsonNode treeNode = codec.readTree(p);
        Iterator<Map.Entry<String, JsonNode>> iterator = treeNode.fields();
        while(iterator.hasNext()) {
            Map.Entry<String, JsonNode> next = iterator.next();
            String fieldName = next.getKey();
            JsonNode nextNode = next.getValue();

            switch(fieldName) {
                case "id" -> routeDefinition.setId(nextNode.asText());
                case "order" -> routeDefinition.setOrder(nextNode.asInt(0));
                case "uri" -> routeDefinition.setUri(URI.create(nextNode.asText()));
                case "predicates" -> routeDefinition.setPredicates(parseDefinition(codec, nextNode, PredicateDefinition.class));
                case "filters" -> routeDefinition.setFilters(parseDefinition(codec, nextNode, FilterDefinition.class));
                default -> {}
            }

        }

        return routeDefinition;
    }

    private <T> List<T> parseDefinition(ObjectMapper objectMapper, JsonNode jsonNode, Class<T> clazz) {
        if (!jsonNode.isArray()) {
            throw new RuntimeException();
        }
        List<T> re = new ArrayList<>();

        jsonNode.elements().forEachRemaining(child -> {
            Object o;
            if (clazz.equals(PredicateDefinition.class)) {
                o = parsePredicate(objectMapper, child);
            } else if(clazz.equals(FilterDefinition.class)) {
                o = parseFilter(objectMapper, child);
            } else {
                throw new RuntimeException("Definition类型异常");
            }
            re.add(clazz.cast(o));
        });
        return re;
    }

    private PredicateDefinition parsePredicate(ObjectMapper objectMapper, JsonNode jsonNode) {
        return PredicateDefinitionDeserializer.parse(objectMapper, jsonNode);
    }

    private FilterDefinition parseFilter(ObjectMapper objectMapper, JsonNode jsonNode) {
        return FilterDefinitionDeserializer.parse(objectMapper, jsonNode);
    }
}