package org.lizhao.cloud.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.utils.JwtUtils;
import org.lizhao.cloud.gateway.configurer.properties.MultipleRedisConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GatewayApplicationTests {

    @Value("${web.security.jwt.key}")
    private byte[] jwtKey;

    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * JwtUtils 测试
     */
    @Test
    public void jwsTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("lizhao", "19960214");
        String jws = JwtUtils.generate(jwtKey, map, Date.valueOf(LocalDateTime.now().plusDays(1).toLocalDate()));
        System.out.println(jws);
        Jws<Claims> parse = JwtUtils.parse(jwtKey, jws);
        System.out.println(parse.getHeader().toString());
        System.out.println(parse.getBody().toString());
        System.out.println(parse.getSignature());
    }

    @Test
    public void rabbitMQTest() {
        Message mes = new Message("ffffff".getBytes());
        this.amqpTemplate.convertAndSend("amq.direct","gateway1", mes);
        byte[] body = Objects.requireNonNull(this.amqpTemplate.receive("gateway1")).getBody();
        System.out.println(new String(body));
    }

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void kafkaProducerTest() {
        try {
            SendResult<String, String> sendResult = kafkaTemplate.send("gateway",0, "test", "springboot test").get();

            System.out.println(sendResult.toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    public void kafkaConsumerTest() {
//
//        ConsumerRecords<String, String> consumerRecord = tt.receive(Collections.singleton(gateway));
//        for (TopicPartition partition : consumerRecord.partitions()) {
//            System.out.println(partition.);
//        }
//    }

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void redisTest() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("gateway:test", "hhhh", 6000, TimeUnit.SECONDS);
//        ops.set("ok", "ok");
    }

    @Resource
    private MultipleRedisConfig multipleRedisConfig;

    @Test
    public void ymlTest() throws JsonProcessingException {
        Map<String, RedisProperties> multipleRedisMap = multipleRedisConfig.getMultipleRedisMap();
        Set<Map.Entry<String, RedisProperties>> entries = multipleRedisMap.entrySet();
        Iterator<Map.Entry<String, RedisProperties>> iterator = entries.iterator();
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        while(iterator.hasNext()) {
            Map.Entry<String, RedisProperties> next = iterator.next();
            String name = next.getKey();
            RedisProperties value = next.getValue();

            System.out.println(name + " : " + objectMapper.writeValueAsString(value));
        }
    }

}
