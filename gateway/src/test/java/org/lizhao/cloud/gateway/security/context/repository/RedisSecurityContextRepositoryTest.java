package org.lizhao.cloud.gateway.security.context.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisSecurityContextRepositoryTest {

    @Resource
    private ReactiveRedisTemplate<String, Authentication> reactiveTokenRedisTemplate;

    @Test
    void load() {
        String tokenValue = "gateway_user:eyJhbGciOiJIUzUxMiJ9.eyJwcmluY2lwYWwiOnsicGFzc3dvcmQiOiIkMmEkMTAkVE9XeE5LUzhlR1dlc3ZQRWl6SFBqT0tPZE1RUDBjNEV0cC9HVXI0UHloYXcuUEJEalNPWWUiLCJ1c2VybmFtZSI6ImxpemhhbyIsImF1dGhvcml0aWVzIjpbXSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiZW5hYmxlZCI6dHJ1ZSwiaWQiOiIyODc4NTk4NjYzNzgyNCIsInBob25lIjoiMTg2NjY0OTY2MTkifSwiY3JlZGVudGlhbHMiOiIkMmEkMTAkVE9XeE5LUzhlR1dlc3ZQRWl6SFBqT0tPZE1RUDBjNEV0cC9HVXI0UHloYXcuUEJEalNPWWUiLCJhdXRob3JpdGllcyI6W10sImV4cCI6MTcxMTg3NDMyNn0.2HdT8dhmp7xGiljZIWCaTMSa2wol2nMwemaGtJNOdqLBvHagbcEAAdl8BMx7QxNxU-pxjnAKrciF40cMGSXTFw";
        reactiveTokenRedisTemplate.keys(tokenValue)
//                .take(1)
//                .next()
                .as(StepVerifier::create)
                .expectNext("gateway_user:eyJhbGciOiJIUzUxMiJ9.eyJwcmluY2lwYWwiOnsicGFzc3dvcmQiOiIkMmEkMTAkVE9XeE5LUzhlR1dlc3ZQRWl6SFBqT0tPZE1RUDBjNEV0cC9HVXI0UHloYXcuUEJEalNPWWUiLCJ1c2VybmFtZSI6ImxpemhhbyIsImF1dGhvcml0aWVzIjpbXSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiZW5hYmxlZCI6dHJ1ZSwiaWQiOiIyODc4NTk4NjYzNzgyNCIsInBob25lIjoiMTg2NjY0OTY2MTkifSwiY3JlZGVudGlhbHMiOiIkMmEkMTAkVE9XeE5LUzhlR1dlc3ZQRWl6SFBqT0tPZE1RUDBjNEV0cC9HVXI0UHloYXcuUEJEalNPWWUiLCJhdXRob3JpdGllcyI6W10sImV4cCI6MTcxMTg3NDMyNn0.2HdT8dhmp7xGiljZIWCaTMSa2wol2nMwemaGtJNOdqLBvHagbcEAAdl8BMx7QxNxU-pxjnAKrciF40cMGSXTFw")
                .verifyComplete();
//                .flatMap(key ->
//                        reactiveTokenRedisTemplate.opsForValue()
//                                .get(key)
//                                .map(SecurityContextImpl::new)
//                );
    }

    @Test
    void save() {
    }

    @Test
    void remove() {
    }
}