package org.lizhao.cloud.gateway.security.jackson2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.lizhao.cloud.gateway.security.authentication.TokenAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;

/**
 * Description TokenAuthenticationToken 序列化
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-31 17:52
 * @since 0.0.1-SNAPSHOT
 */
public class TokenAuthenticationTokenDeserializer extends JsonDeserializer<TokenAuthenticationToken> {
    private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST = new TypeReference<List<GrantedAuthority>>() {
    };

    private static final TypeReference<Object> OBJECT = new TypeReference<Object>() {
    };

    private static final TypeReference<String> STRING = new TypeReference<String>() {
    };


    /**
     * This method construct {@link UsernamePasswordAuthenticationToken} object from
     * serialized json.
     * @param jp the JsonParser
     * @param ctxt the DeserializationContext
     * @return the user
     * @throws IOException if a exception during IO occurs
     * @throws JsonProcessingException if an error during JSON processing occurs
     */
    @Override
    public TokenAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        boolean authenticated = readJsonNode(jsonNode, "authenticated").asBoolean();
        JsonNode principalNode = readJsonNode(jsonNode, "principal");
        Object principal = getPrincipal(mapper, principalNode);
        JsonNode credentialsNode = readJsonNode(jsonNode, "credentials");
        Object credentials = getCredentials(credentialsNode);
        List<GrantedAuthority> authorities = mapper.readValue(readJsonNode(jsonNode, "authorities").traverse(mapper),
                GRANTED_AUTHORITY_LIST);
        String tokenStr = readJsonNode(jsonNode, "token").asText(null);
        JsonNode detailsNode = readJsonNode(jsonNode, "details");
        Object details = null;
        if (!(detailsNode.isNull() || detailsNode.isMissingNode())) {
            details = mapper.readValue(detailsNode.toString(), OBJECT);
        }

        return (!authenticated)
                ? TokenAuthenticationToken.unauthenticated(principal, credentials)
                : TokenAuthenticationToken.authenticated(tokenStr, principal, credentials, details, authorities);
    }

    private Object getCredentials(JsonNode credentialsNode) {
        if (credentialsNode.isNull() || credentialsNode.isMissingNode()) {
            return null;
        }
        return credentialsNode.asText();
    }

    private Object getPrincipal(ObjectMapper mapper, JsonNode principalNode)
            throws IOException, JsonParseException, JsonMappingException {
        if (principalNode.isObject()) {
            return mapper.readValue(principalNode.traverse(mapper), Object.class);
        }
        return principalNode.asText();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
