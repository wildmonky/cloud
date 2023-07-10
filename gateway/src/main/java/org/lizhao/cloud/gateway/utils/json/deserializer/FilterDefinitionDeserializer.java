package org.lizhao.cloud.gateway.utils.json.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.lizhao.base.utils.reflect.ReflectUtil;
import org.springframework.cloud.gateway.filter.FilterDefinition;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Description FilterDefinition 反序列化
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-03 22:49
 * @since 0.0.1-SNAPSHOT
 */
public class FilterDefinitionDeserializer extends JsonDeserializer<FilterDefinition> {

    private static final String FILTER_DEFINITION_PACKAGE = "org.lizhao.cloud.gateway.model.filterDefinition";

    @Override
    public FilterDefinition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return parse(p.getCodec().readTree(p));
    }

    public static FilterDefinition parse(JsonNode rootNode) {
        String field = ReflectUtil.upperFirstChar(rootNode.get("name").asText());
        try {
            List<Object> args = new ArrayList<>();
            switch (field) {
                case "AddRequestHeader", "AddResponseHeader" -> {
                    args.add(rootNode.get("headerName").asText());
                    args.add(rootNode.get("headerValue").asText());
                }
                case "AddRequestHeadersIfNotPresent" -> {
                    JsonNode node = rootNode.get("headerMap");
                    Map<String, String> map = new HashMap<>();
                    node.fields().forEachRemaining(ite -> {
                        map.put(ite.getKey(), ite.getValue().asText(""));
                    });
                    args.add(map);
                }
                case "AddRequestParameter" -> {
                    args.add(rootNode.get("paramName").asText());
                    args.add(rootNode.get("paramValue").asText());;
                }
                case "CircuitBreaker" -> {
                    args.add(rootNode.get("circuitBreakerName").asText());
                    args.add(rootNode.get("fallBackUri").asText());
                    JsonNode node = rootNode.get("statusCodeSet");
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
