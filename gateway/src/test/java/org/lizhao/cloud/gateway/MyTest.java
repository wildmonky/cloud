package org.lizhao.cloud.gateway;

import org.junit.jupiter.api.Test;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.springframework.util.AntPathMatcher;

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

}
