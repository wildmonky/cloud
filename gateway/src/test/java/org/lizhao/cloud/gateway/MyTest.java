package org.lizhao.cloud.gateway;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description 测试类 无需Spring context
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-25 17:47
 * @since 0.0.1-SNAPSHOT
 */

public class MyTest {

    @Test
    public void test() {
        SnowFlake snowFlake = new SnowFlake(1L, 1L, 0L);
        Object generate = snowFlake.generate(null, null, null, null);
        System.out.printf("%064d%n", generate);
        System.out.printf("|%10s|%n", Long.toBinaryString((long)generate)); // 使用0填充
//        ByteBuffer bb = ByteBuffer.allocate(Long.SIZE/Byte.SIZE);
//        bb.putLong(System.currentTimeMillis());
//        System.out.println(bb.array().length);
    }

    @Test
    public void regexTest() {
        Pattern pattern = Pattern.compile("(:)(\\w*$)");
        Matcher matcher = pattern.matcher("password-update:lizhao");
        while (matcher.find()) {
            System.out.println(matcher.group() + " start: " + matcher.start() + " stop: " + matcher.end());
        }
    }

    @Test
    public void regexTest1() {
        Pattern pattern = Pattern.compile(".*/test");
        Matcher matcher = pattern.matcher("/gateway/route/test");
        System.out.println(matcher.matches());
    }
    @Test
    public void regexTest2() {
        Pattern pattern = Pattern.compile("/gateway/*");
        Matcher matcher = pattern.matcher("/gateway/route/test");
        System.out.println(matcher.matches());
    }

    @Test
    public void antPathTest() {
        AntPathMatcher matcher = new AntPathMatcher();
        System.out.println(matcher.match("/login", "/login"));
    }

    /**
     * 当 json 属性为对象时
     * @throws JsonProcessingException
     */
    @Test
    public void jsonMapTest() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
        sm.addKeySerializer(User.class, new JsonSerializer<User>() {
            @Override
            public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeFieldName(om.writeValueAsString(value));
            }
        });
        sm.addKeyDeserializer(User.class, new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return om.readValue(key, User.class);
            }
        });
        om.registerModule(sm);
        Map<User, List<Group>> map = new HashMap<>();
        User user = new User();
        user.setId("1111");
        Group group = new Group();
        group.setId("2222");
        map.put(user, Collections.singletonList(group));
        System.out.println(om.writeValueAsString(map));

//        JsonParser parser = om.createParser("{\"{\\\"createUseId\\\":null,\\\"createUseName\\\":null,\\\"createTime\\\":null,\\\"updateUseId\\\":null,\\\"updateUseName\\\":null,\\\"updateTime\\\":null,\\\"id\\\":\\\"1111\\\",\\\"phone\\\":null,\\\"name\\\":null,\\\"password\\\":null,\\\"status\\\":null}\":[{\"createUseId\":null,\"createUseName\":null,\"createTime\":null,\"updateUseId\":null,\"updateUseName\":null,\"updateTime\":null,\"id\":\"2222\",\"parentId\":null,\"name\":null,\"status\":null,\"child\":null}]}");
//        TypeReference<Map<User, List<Group>>> typeReference = new TypeReference<Map<User, List<Group>>>() {};
//        map = om.readValue("{\"{\\\"createUseId\\\":null,\\\"createUseName\\\":null,\\\"createTime\\\":null,\\\"updateUseId\\\":null,\\\"updateUseName\\\":null,\\\"updateTime\\\":null,\\\"id\\\":\\\"1111\\\",\\\"phone\\\":null,\\\"name\\\":null,\\\"password\\\":null,\\\"status\\\":null}\":[{\"createUseId\":null,\"createUseName\":null,\"createTime\":null,\"updateUseId\":null,\"updateUseName\":null,\"updateTime\":null,\"id\":\"2222\",\"parentId\":null,\"name\":null,\"status\":null,\"child\":null}]}", typeReference);
//
//        System.out.println(map);
    }

    @Test
    public void fluxTest() {
        Flux<String> flux = Flux.create(fluxSink -> {

            fluxSink.next("ssss");
            fluxSink.next("hhhh");

            fluxSink.complete();
        });

        flux.concatMap(s -> {
            System.out.println(s);
            return Mono.just(s);
        })
                .hasElements()
                .subscribe(System.out::println);

    }

}
