package org.lizhao.base.utils;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <code>@ConfigurationPropertiesScan(basePackages = {"org.lizhao.cloud.gateway.configurer.properties"})</code><br/>
 * <code>@EnableAspectJAutoProxy</code><br/>
 * <code>@EnableRedisRepositories</code><br/>
 * <code>@SpringBootApplication(scanBasePackages = {"org.lizhao.cloud.gateway"})</code><br/>
 * public class GatewayApplication {<br/>
 *     public static void main(String[] args) {<br/>
 *         ConfigurableApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);<br/>
 *         SpringUtils.setApplicationContext(applicationContext);<br/>
 *     }<br/>
 * }
 * ApplicationContext 持有者，并包含一些通用方法
 */
public class SpringUtils {

    public static ConfigurableApplicationContext applicationContext;

    private static final ApplicationHome applicationHome = new ApplicationHome(SpringUtils.class);

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        SpringUtils.applicationContext = applicationContext;
    }

    public static String getProperty(String propertyName) {
        return applicationContext.getEnvironment().getProperty(propertyName);
    }

    public static boolean containsProperty(String propertyName) {
        return applicationContext.getEnvironment().containsProperty(propertyName);
    }

    /**
     * 根据启动类位置判断是否以jar方式启动
     * @return true-以jar包方式启动
     */
    public static boolean isRunAsJar() {
//        String protocol = Objects.requireNonNull(application.getMainApplicationClass().getResource("")).getProtocol();
        String protocol = getAppHome();
        return  protocol != null && protocol.contains("jar");
    }

    public static String getAppHome() {
        return applicationHome.getSource().toString();
    }
}

