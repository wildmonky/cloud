package org.lizhao.cloud.gateway;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.Node;
import org.lizhao.base.model.UserInfo;
import org.lizhao.base.utils.WebUtils;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.cloud.gateway.json.definition.RouteDefinitionDeserializer;
import org.lizhao.cloud.gateway.json.userModel.UserInfoSerializer;
import org.lizhao.cloud.gateway.model.predicateDefinition.PathPredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
//        Pattern pattern = Pattern.compile("(:)(\\w*$)");
//        Matcher matcher = pattern.matcher("password-update:lizhao");
//        while (matcher.find()) {
//            System.out.println(matcher.group() + " start: " + matcher.start() + " stop: " + matcher.end());
//        }
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher("18666496619");
        System.out.println(matcher.matches());

        while (matcher.find()) {
            System.out.println(matcher.group());
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
    public void nodeTest() throws JsonProcessingException {
        Node<Group> node = new Node<>();
        Group current = new Group();
        current.setId("124578963");
        current.setName("当前节点");
        node.set(current);

        Node<Group> children = new Node<>();
        Group g2 = new Group();
        g2.setName("测试孩子");
        children.set(g2);

        HashSet<Node<Group>> c = new HashSet<>();
        c.add(children);

        node.setChildren(c);

        ObjectMapper om = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
//        sm.addSerializer(Node.class, new JsonSerializer<Node<?>>() {
//            @Override
//            public void serialize(Node<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                Object o = value.get();
//                Set<?> children = value.getChildren();
//
//                om.w .readValue(o, o.getClass())
//
//
//            }
//        })

        System.out.println(om.writeValueAsString(node));

    }

    @Test
    public void DateTimeFormatterTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();

        final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormat));
        simpleModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                String text = p.getText();
                if (StringUtils.isNotBlank(text)) {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(text.trim(), dateTimeFormat);
                    return zonedDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }
                return null;
            }
        });
        simpleModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                ZonedDateTime zonedDateTime = value.atZone(ZoneId.systemDefault());
                gen.writeString(zonedDateTime.format(dateTimeFormat));
            }
        });
        objectMapper.registerModule(simpleModule);
        try{
            String s = objectMapper.writeValueAsString(LocalDateTime.now());
            System.out.println(s);
            String ss ="{\"groups\":null,\"roles\":null,\"authorities\":[],\"createUseId\":null,\"createUseName\":null,\"createTime\":\"2024-03-25T20:52:11.371434+08:00\",\"updateUseId\":\"141441121\",\"updateUseName\":\"lizhao\",\"updateTime\":\"2024-03-26T17:17:50.675977+08:00\",\"id\":\"28785986637824\",\"phone\":\"18666496619\",\"name\":\"lizhao\",\"password\":\"{bcrypt}$2a$10$4gyDA8lBCyZq7KQPKbRG4uaMB5R1HV2ZfokITKH9sFtRAUlKaF3Au\",\"status\":1,\"originAuthorities\":null,\"token\":null,\"grantedAuthorities\":[],\"enabled\":true,\"username\":\"lizhao\",\"accountNonExpired\":true,\"credentialsNonExpired\":true,\"accountNonLocked\":true}";
            System.out.println(objectMapper.readValue(ss, UserInfo.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void routeDefinitionJsonTest() throws IOException {
        ObjectMapper om = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
        sm.addDeserializer(RouteDefinition.class, new RouteDefinitionDeserializer());
        om.registerModule(sm);

        RouteDefinition rd = new RouteDefinition();
        rd.setId("test");
        rd.setUri(URI.create("https://www.baidu.com"));
        PathPredicateDefinition predicate = new PathPredicateDefinition("/hhh");
        rd.setPredicates(Collections.singletonList(predicate));
        String s = "{" +
                        "\"id\":\"test\"," +
                        "\"predicates\":[" +
                            "{" +
                                "\"name\":\"Path\"," +
                                "\"args\":{" +
                                    "\"_genkey_0\":\"/hhh\"" +
                                "}," +
                                "\"paths\":[\"/hhh\"]" +
                            "}" +
                        "]," +
                        "\"filters\":[]," +
                        "\"uri\":\"https://www.baidu.com\"," +
                        "\"metadata\":{}," +
                        "\"order\":0" +
                   "}";

        byte[] bytes = s.getBytes();
        RouteDefinition definition = om.readValue(bytes, 0, bytes.length, RouteDefinition.class);
        System.out.println(definition);
//        RouteDefinition routeDefinition = om.readValue("{\"id\":\"test\",\"predicates\":[{\"name\":\"Path\",\"args\":{\"_genkey_0\":\"/hhh\"},\"paths\":[\"/hhh\"]}],\"filters\":[],\"uri\":\"https://www.baidu.com\",\"metadata\":{},\"order\":0}", RouteDefinition.class);
//        System.out.println(routeDefinition);

    }

    @Test
    public void passwordEncoder() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.matches("123456", "{bcrypt}$2a$10$.QZw0bT25ZJaNPBBE4hhg.FjMPKiZyc8tKk2w.zblY5Y3DnLPo.L2"));
        System.out.println(passwordEncoder.matches("123456", "{bcrypt}$2a$10$Np4tysrdi2rH02qxO.5gtOPMOpFgfKYSlIxmpgGj5gCzYmPdq1SWe"));
//        System.out.println(passwordEncoder.matches("123456", "$2a$10$BQ/279bMtZVzrVLvOMHMJ.LasSVK4U3gr3Brtdb/lei5Y/Gjv44Ye"));
    }

    @Test
    public void domainTest() {
        String[] urls = new String[]{"https://www.badiu.com/sss", "http://localhost:8083/test"};
        Pattern p = Pattern.compile("(?<=https?://)([\\w.-]+)(?=[^\\w.-]|$)",Pattern.CASE_INSENSITIVE);
        for (String url : urls) {
            Matcher matcher = p.matcher(url);
            if (matcher.find()) {
                System.out.println(matcher.group());
            }
        }
    }

    @Test
    public void userModelSerializerTest() throws JsonProcessingException {
        Group group = new Group();
        group.setName("组");

        Role role = new Role();
        role.setName("角色");

        Authority authority = new Authority();
        authority.setName("权限");

        UserInfo userModel = new UserInfo();
        userModel.setId("测试");
        userModel.setCreateTime(LocalDateTime.now());
//        userModel.setGroups(Collections.singleton(group));
        userModel.setRoles(Collections.singleton(role));
        userModel.setOriginAuthorities(Collections.singleton(authority));

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(UserInfo.class, new UserInfoSerializer());
        objectMapper.registerModule(module);

        System.out.println(objectMapper.writeValueAsString(userModel));

    }

    @Test
    public void classTest() {
        Class<Group> groupClass = Group.class;
        System.out.println(groupClass.getPackageName());
        String[] split = Group.class.getName().split("\\.");
        System.out.println(split[split.length - 1]);
    }

    @Test
    public void httpsTest() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream("D:\\Repository\\WorkSpace\\docker\\cert.pem"), "19960214".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "19960214".toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            URL url = new URL("https://106.52.188.209:2375");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());

            connection.setHostnameVerifier((host, sslSession) -> {
                System.out.println("远程主机：" + host);
                return false;
            });

            InputStream response = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                byte[] bs = new byte[1024];
                while (response.available() > 0) {
                    response.read(bs);
                    outputStream.writeBytes(bs);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                response.close();
                connection.disconnect();
            }


            System.out.println(outputStream.toString("GBK"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void domainTst() {
        System.out.println(WebUtils.paresHost("106.52.188.209:8081"));
    }

}
