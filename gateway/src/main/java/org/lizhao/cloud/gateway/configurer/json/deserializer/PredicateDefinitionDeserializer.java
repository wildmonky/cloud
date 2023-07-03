package org.lizhao.cloud.gateway.configurer.json.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.lizhao.base.utils.reflect.ReflectUtil;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return parse(p.getCodec().readTree(p));
    }

    public static PredicateDefinition parse(JsonNode rootNode) {
        String name = ReflectUtil.upperFirstChar(rootNode.get("name").asText());
        List<String> args = new ArrayList<>();
        try {
            switch (name) {
                case "After", "Before" -> args.add(rootNode.get("time").asText());
                case "Between" -> args.addAll(rootNode.findValuesAsText("timeList"));
                case "Header" -> {
                    args.add(rootNode.get("headerName").asText());
                    args.add(rootNode.get("regex").asText());
                }
                case "Host" -> args.addAll(rootNode.findValuesAsText("hostPatternSet"));
                case "Method" -> args.addAll(rootNode.findValuesAsText("httpMethodSet"));
                case "Path" -> args.addAll(rootNode.findValuesAsText("pathMatcherSet"));
                case "Query" -> {
                    args.add(rootNode.get("paramName").asText());
                    args.add(rootNode.get("paramValue").asText());
                }
                case "RemoteAddr" -> args.add(rootNode.get("remoteAddr").asText());
                case "XForwardedRemoteAddr" -> args.addAll(rootNode.findValuesAsText("xForwardedRemoteAddrSet"));
                case "Weight" -> {
                    args.add(rootNode.get("group").asText());
                    args.add(rootNode.get("weight").asText());
                }
                default -> throw new RuntimeException(String.format("未知的%1$sPredicateDefinition", name));
            }
            List<? extends Class<?>> argsTypeList = args.stream().map(Object::getClass).toList();
            Class<?>[] argTypes = argsTypeList.toArray(new Class<?>[0]);
            Class<?> forName = Class.forName(PREDICATE_DEFINITION_PACKAGE + "." + name + "PredicateDefinition");
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未发现对应的构造函数", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("实例化方法调用失败", e);
        }
    }
}
