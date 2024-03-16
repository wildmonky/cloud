package org.lizhao.cloud.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.lizhao.base.utils.JwtUtils;
import org.lizhao.cloud.gateway.controller.RouteController;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class GatewayApplicationTests {

    @Resource
    private RouteController routeController;
    @Value("${web.jwt.key}")
    private byte[] jwtKey;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Test
    void contextLoads() {
        System.out.println(routeController.listTest());
    }

    @Test
    public void test() {
        Pattern pattern = Pattern.compile(".*/test");
        Matcher matcher = pattern.matcher("/gateway/route/test");
        System.out.println(matcher.matches());
    }

    @Test
    public void test1() {
        Pattern pattern = Pattern.compile(".*/test");
        Matcher matcher = pattern.matcher("/gateway/route/test");
        System.out.println(matcher.matches());
    }

    /**
     * JwtUtils 测试
     */
    @Test
    public void test2() {
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
    private KafkaTemplate<String, String> tt;

    @Test
    public void kafkaProducerTest() {
        try {
            SendResult<String, String> sendResult = tt.send("gateway",0, "test", "springboot test").get();
            System.out.println(sendResult.toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    public void kafkaConsumerTest() {
//        TopicPartitionOffset gateway = new TopicPartitionOffset("gateway", 0, 0L);
//
//        tt.setConsumerFactory(consumerFactory);
//        ConsumerRecords<String, String> consumerRecord = tt.receive(Collections.singleton(gateway));
//        for (TopicPartition partition : consumerRecord.partitions()) {
//            System.out.println(partition.);
//        }
//    }

}
