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

    private static final Pattern URL_PATTERN = Pattern.compile("(?<=https?://)([\\w.-]+)(?=[^\\w.-]|$)",Pattern.CASE_INSENSITIVE);

    private static final Pattern IP_PATTERN = Pattern.compile("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(:\\d{0,5})?", Pattern.CASE_INSENSITIVE);

    private static final Pattern DOMAIN_PATTERN = Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?", Pattern.CASE_INSENSITIVE);

    private static final String IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)";

    private static final String DOT = ":";

    private static final String PORT = "\\d{0,5}";

    public static String extractDomain(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new RuntimeException("域名解析异常：" + url);
    }

    public static String paresHost(String host) {
        Matcher matcher = DOMAIN_PATTERN.matcher(host);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new RuntimeException("域名解析异常：" + host);
    }
}
