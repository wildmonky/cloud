package org.lizhao.base.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.lizhao.base.exception.CustomException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * description JWT 工具类 使用前请配置密钥
 * 1、生成JWT
 * 2、验证JWT
 *
 * @author Administrator
 * @version V1.0
 * @date 2023/7/28 15:18:23
 */
public class JwtUtils {

    /**
     * 生成 jwt
     *
     * @param secretKey 密钥
     * @param payload 载荷
     * @return jwt
     */
    public static String generate(SecretKey secretKey, Map<String, Object> payload, Date expireDate) {
        if (secretKey == null) {
            throw new CustomException("未配置JWT密钥");
        }
        return Jwts.builder().signWith(secretKey).setClaims(payload).setExpiration(expireDate).compact();
    }

    /**
     * 生成 jwt
     *
     * @param secretKey 密钥
     * @param payload 载荷
     * @return jwt
     */
    public static String generate(String secretKey, Map<String, Object> payload, Date expireDate) {
        if (secretKey == null) {
            throw new CustomException("未配置JWT密钥");
        }
        return generate(secretKey.getBytes(), payload, expireDate);
    }

    /**
     * 生成 jwt
     *
     * @param secretKey 密钥
     * @param payload 载荷
     * @return jwt
     */
    public static String generate(byte[] secretKey, Map<String, Object> payload, Date expireDate) {
        if (secretKey == null) {
            throw new CustomException("未配置JWT密钥");
        }
        return generate(buildDefaultKey(secretKey), payload, expireDate);
    }

    /**
     * 根据密钥解析jwt
     *
     * @param secretKey 密钥
     * @param jws jwt string
     * @return 解析后的jwt
     */
    public static Jws<Claims> parse(byte[] secretKey, String jws) {
        if (secretKey == null) {
            throw new CustomException("JWT密钥为空");
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jws);
    }

    /**
     * 根据密钥解析jwt
     *
     * @param secretKey 密钥
     * @param jws jwt string
     * @return 解析后的jwt
     */
    public static Jws<Claims> parse(SecretKey secretKey, String jws) {
        if (secretKey == null) {
            throw new CustomException("JWT密钥为空");
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jws);
    }

    /**
     * 根据密钥验证jwt
     *
     * @param secretKey 密钥
     * @param jws jwt string
     * @param func 验证逻辑
     * @return true-验证成功；false-验证失败
     */
    public static boolean verify(SecretKey secretKey, String jws, Function<Jws<Claims>, Boolean> func) {
        Jws<Claims> parse = parse(secretKey, jws);
        return func.apply(parse);
    }

    /**
     * 根据密钥验证jwt
     *
     * @param secretKey 密钥
     * @param jws jwt string
     * @param func 验证逻辑
     * @return true-验证成功；false-验证失败
     */
    public static boolean verify(byte[] secretKey, String jws, Function<Jws<Claims>, Boolean> func) {
        Jws<Claims> parse = parse(secretKey, jws);
        return func.apply(parse);
    }

    /**
     * 根据key的长度判断其使用的HMAC-SHA算法类型，从而创建 {@link SecretKeySpec} 对象
     * 只支持 {@link SignatureAlgorithm#HS512}, {@link SignatureAlgorithm#HS384}, {@link SignatureAlgorithm#HS256}
     * @see Keys#hmacShaKeyFor
     */
    public static SecretKey buildDefaultKey(byte[] key) {
        if (key == null || key.length == 0) {
            throw new CustomException("未配置默认JWT密钥");
        }
        return Keys.hmacShaKeyFor(key);
    }

}
