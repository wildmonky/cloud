package org.lizhao.cloud.gateway.utils.json.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.IOException;

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
        return null;
    }
}
