package org.lizhao.cloud.gateway.json.definition;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lizhao.cloud.gateway.json.JsonUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;

import java.io.IOException;

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

    private static final String FILTER_DEFINITION_SUFFIX = "FilterDefinition";

    @Override
    public FilterDefinition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper)p.getCodec();
        return parse(objectMapper, objectMapper.readTree(p));
    }

    public static FilterDefinition parse(ObjectMapper objectMapper, JsonNode rootNode) {
        return JsonUtils.parse(FilterDefinition.class, FILTER_DEFINITION_PACKAGE, FILTER_DEFINITION_SUFFIX, objectMapper, rootNode);
    }

}
