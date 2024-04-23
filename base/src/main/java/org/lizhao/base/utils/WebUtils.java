package org.lizhao.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description Web工具
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-17 22:15
 * @since 0.0.1-SNAPSHOT
 */
public class WebUtils {

    private static final Pattern DOMAIN_PATTERN = Pattern.compile("(?<=https?://)([\\w.-]+)(?=[^\\w.-]|$)",Pattern.CASE_INSENSITIVE);
    public static String extractDomain(String url) {
        Matcher matcher = DOMAIN_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new RuntimeException("域名解析异常：" + url);
    }


}
