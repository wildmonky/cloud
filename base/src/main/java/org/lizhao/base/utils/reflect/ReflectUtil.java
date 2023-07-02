package org.lizhao.base.utils.reflect;

import jakarta.annotation.Nonnull;
import org.springframework.util.ReflectionUtils;

/**
 * Description 反射工具类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-01 18:14
 * @since 0.0.1-SNAPSHOT
 */
public class ReflectUtil {

    /**
     * 首字母大写
     * @param str 需要首字母大写的字符串
     * @return str首字母大写
     */
    public static String upperFirstChar(@Nonnull String str) {
        char[] charArray = str.toCharArray();
        char firstChar = charArray[0];
        if (97 <= firstChar & firstChar <= 127) {
            firstChar ^= 32;
        }
        charArray[0] = firstChar;
        return String.valueOf(charArray);
    }

}
