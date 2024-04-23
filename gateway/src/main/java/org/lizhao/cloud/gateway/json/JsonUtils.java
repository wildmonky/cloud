package org.lizhao.cloud.gateway.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.*;
import jakarta.validation.constraints.NotNull;
import org.lizhao.base.utils.reflect.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-22 12:26
 * @since 0.0.1-SNAPSHOT
 */
public class JsonUtils {

    public static <T> T parse(Class<T> targetClass, String classPrefix, String classSuffix, ObjectMapper objectMapper, JsonNode rootNode) {
        String name = ReflectUtil.upperFirstChar(rootNode.get("name").asText());

        Map<String, Object> argsMap = new HashMap<>();

        Iterator<Map.Entry<String, JsonNode>> iterator = rootNode.fields();
        ObjectReader arrayReader = objectMapper.readerForArrayOf(String.class);
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> next = iterator.next();
            String fieldName = next.getKey();
            JsonNode jsonNode = next.getValue();

            if (jsonNode.isArray()) {
                try{
                    Object o = arrayReader.readValue(jsonNode);
                    argsMap.put(fieldName, o);
                }catch(IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                argsMap.put(fieldName, extractValueInNode(jsonNode));
            }
        }

        Map<Constructor<?>, List<String>> constructorParameterMap = new HashMap<>();
        try{
            Class<?> clazz = Class.forName(classPrefix + "." + name + classSuffix);
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<?> finalConstructor = Arrays.stream(constructors).filter(constructor -> {
                Parameter[] parameters = constructor.getParameters();
                ArrayList<String> parameterNames = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    NotNull notnullAnnotation = parameter.getAnnotation(NotNull.class);
                    String parameterName = parameter.getName();
                    Class<?> parameterType = parameter.getType();
                    Object parameterValue = argsMap.get(parameterName);

                    if (notnullAnnotation != null && parameterValue == null) {
                        return false;
                    }

                    if (parameterValue != null && !parameterValue.getClass().isAssignableFrom(parameterType)) {
                        return false;
                    }
                    parameterNames.add(parameterName);
                }
                constructorParameterMap.put(constructor, parameterNames);
                return true;
            }).findFirst().orElseThrow(() -> new RuntimeException("未获取到对应的构造函数"));

            List<String> parameters = constructorParameterMap.get(finalConstructor);
            ArrayList<Object> parameterValues = new ArrayList<>();
            for (String parameterName : parameters) {
                parameterValues.add(argsMap.get(parameterName));
            }

            Object o = finalConstructor.newInstance(parameterValues.toArray(new Object[0]));
            return targetClass.cast(o);
        }catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private static <T> T extractValueInNode(JsonNode jsonNode) {
        T value;
        JsonNodeType nodeType = jsonNode.getNodeType();
        switch(nodeType) {
            case ARRAY -> {
                Set<Object> set = new HashSet<>(jsonNode.size());
                Iterator<JsonNode> elements = jsonNode.elements();
                while (elements.hasNext()) {
                    set.add(extractValueInNode(elements.next()));
                }
                return (T) set;
            }
            case BINARY -> value = (T) ((BinaryNode)jsonNode).binaryValue();
            case BOOLEAN -> value = (T) Boolean.valueOf(jsonNode.asBoolean());
//            case MISSING, NULL -> value = null;
            case NUMBER -> value = (T) jsonNode.numberValue();
            case OBJECT -> value = (T) jsonNode.asText();
            case POJO -> value = (T) ((POJONode)jsonNode).getPojo();
            case STRING -> value = (T) jsonNode.asText();
            default -> value = null;
        }
        return value;
    }


}
