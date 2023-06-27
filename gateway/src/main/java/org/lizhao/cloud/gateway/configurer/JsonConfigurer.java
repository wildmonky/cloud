package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-06-27 23:25
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class JsonConfigurer {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.deserializerByType(RouteDefinition.class, new JsonDeserializer<RouteDefinition>() {
            @Override
            public RouteDefinition deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                //TODO json 序列化
                RouteDefinition routeDefinition = new RouteDefinition();

                ObjectCodec codec = jsonParser.getCodec();
                TreeNode treeNode = codec.readTree(jsonParser);
                TreeNode idNode = treeNode.get("id");
                if (idNode.isValueNode()) {

                }


                return routeDefinition;
            }
        });


        return new MappingJackson2HttpMessageConverter(builder.build());
    }

}
