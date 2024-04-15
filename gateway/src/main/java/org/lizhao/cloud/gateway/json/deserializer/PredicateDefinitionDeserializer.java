package org.lizhao.cloud.gateway.json.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.validation.constraints.NotNull;
import org.lizhao.base.utils.reflect.ReflectUtil;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * description
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/7/3 19:02:06
 */
public class PredicateDefinitionDeserializer extends JsonDeserializer<PredicateDefinition> {

    private static final String PREDICATE_DEFINITION_PACKAGE = "org.lizhao.cloud.gateway.model.predicateDefinition";

    @Override
    public PredicateDefinition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return parse((ObjectMapper) p.getCodec(), p.getCodec().readTree(p));
    }

    public static PredicateDefinition parse(ObjectMapper objectMapper, JsonNode rootNode) {
        String name = ReflectUtil.upperFirstChar(rootNode.get("name").asText());
        List<Object> args = new ArrayList<>();

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
                argsMap.put(fieldName, jsonNode.asText());
            }
        }

        Map<Constructor<?>, List<String>> constructorParameterMap = new HashMap<>();
        try{
            Class<?> forName = Class.forName(PREDICATE_DEFINITION_PACKAGE + "." + name + "PredicateDefinition");
            Constructor<?>[] constructors = forName.getConstructors();
            Constructor<?> finalConstructor = Arrays.stream(constructors).filter(constructor -> {
                Parameter[] parameters = constructor.getParameters();
                ArrayList<String> parameterNames = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    NotNull annotation = parameter.getAnnotation(NotNull.class);
                    String parameterName = parameter.getName();
                    Class<?> parameterType = parameter.getType();
                    Object parameterValue = argsMap.get(parameterName);

                    if (annotation != null && parameterValue == null) {
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
            return (PredicateDefinition) o;
        }catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

//        try {
//            switch (name) {
//                case "After", "Before" -> args.add(rootNode.get("time").asText());
//                case "Between" -> {
//                    args.add(rootNode.get("startTime").asText());
//                    args.add(rootNode.get("endTime").asText());
//                }
//                case "Header" -> {
//                    args.add(rootNode.get("headerName").asText());
//                    args.add(rootNode.get("regex").asText());
//                }
//                case "Host" -> {
//                    JsonNode node = rootNode.get("hostPatternSet");
//                    Collection collection = objectMapper.treeToValue(node, Collection.class);
//                    args.add(collection);
//                }
//                case "Method" -> {
//                    JsonNode node = rootNode.get("httpMethodSet");
//                    Collection collection = objectMapper.treeToValue(node, Collection.class);
//                    args.add(collection);
//                }
//                case "Path" -> {
//                    JsonNode node = rootNode.get("pathMatcherSet");
//                    Collection collection = objectMapper.treeToValue(node, Collection.class);
//                    args.add(collection);
//                }
//                case "Query" -> {
//                    args.add(rootNode.get("paramName").asText());
//                    args.add(rootNode.get("paramValue").asText());
//                }
//                case "RemoteAddr" -> args.add(rootNode.get("remoteAddr").asText());
//                case "XForwardedRemoteAddr" -> {
//                    JsonNode node = rootNode.get("xForwardedRemoteAddrSet");
//                    Collection collection = objectMapper.treeToValue(node, Collection.class);
//                    args.add(collection);
//                }
//                case "Weight" -> {
//                    args.add(rootNode.get("group").asText());
//                    args.add(rootNode.get("weight").asText());
//                }
//                default -> throw new RuntimeException(String.format("未知的%1$sPredicateDefinition", name));
//            }
//            List<? extends Class<?>> argsTypeList = args.stream().map(Object::getClass).toList();
//            Class<?>[] argTypes = argsTypeList.toArray(new Class<?>[0]);
//            Class<?> forName = Class.forName(PREDICATE_DEFINITION_PACKAGE + "." + name + "PredicateDefinition");
//            Constructor<?>[] constructors = forName.getConstructors();
//            Constructor<?> finalConstructor = Arrays.stream(constructors).filter(constructor -> {
//                Class<?>[] parameterTypes = constructor.getParameterTypes();
//                for (int i = 0; i < parameterTypes.length; i++) {
//                    Class<?> parameterType = parameterTypes[i];
//                    if (!parameterType.isAssignableFrom(argTypes[i])) {
//                        return false;
//                    }
//                }
//                return true;
//            }).findFirst().orElseThrow(() -> new RuntimeException("未获取到对应的构造函数"));
//            Object o = finalConstructor.newInstance(args.toArray(new Object[0]));
//            return (PredicateDefinition) o;
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("未发现对应的构造函数", e);
//        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException("实例化方法调用失败", e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
