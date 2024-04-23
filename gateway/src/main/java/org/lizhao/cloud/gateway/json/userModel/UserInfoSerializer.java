package org.lizhao.cloud.gateway.json.userModel;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.model.UserInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-20 12:55
 * @since 0.0.1-SNAPSHOT
 */
public class UserInfoSerializer extends JsonSerializer<UserInfo> {
    @Override
    public void serialize(UserInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        Set<Group> groups = Optional.ofNullable(value.getGroups()).orElse(Collections.emptySet());
        Set<Role> roles = Optional.ofNullable(value.getRoles()).orElse(Collections.emptySet());
        Set<Authority> authorities = Optional.ofNullable(value.getOriginAuthorities()).orElse(Collections.emptySet());

        Class<?> superclass = value.getClass().getSuperclass();

        gen.writeStartObject();

        writeSuperClassField(superclass, value, gen);

        gen.writeFieldName("groups");
        gen.writeStartArray("groups", groups.size());

        for (Group group : groups) {
            gen.writeString(group.getName());
        }

        gen.writeEndArray();

        gen.writeFieldName("roles");
        gen.writeStartArray("roles", roles.size());

        for (Role role : roles) {
            gen.writeString(role.getName());
        }

        gen.writeEndArray();

        gen.writeFieldName("authorities");
        gen.writeStartArray("authorities", authorities.size());

        for (Authority authority : authorities) {
            gen.writeString(authority.getName());
        }

        gen.writeEndArray();

        gen.writeEndObject();

        gen.flush();
        gen.close();
    }

    private void writeSuperClassField(Class<?> clazz, UserInfo userModel, JsonGenerator gen) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object fieldValue = field.get(userModel);
                if (fieldValue != null) {
                    gen.writeObjectField(fieldName, fieldValue);
                }
            } catch (IllegalAccessException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            writeSuperClassField(superclass, userModel, gen);
        }
    }

}
