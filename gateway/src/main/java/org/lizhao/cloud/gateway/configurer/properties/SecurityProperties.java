package org.lizhao.cloud.gateway.configurer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description 自定义配置
 * 需要加载依赖
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-configuration-processor</artifactId>
 *   <optional>true</optional>
 * </dependency>
 * 配合注解@ConfigurationPropertiesScan或@EnableConfigurationProperties
 * 之后需要 mvn clean mvn install IDEA才能在配置文件中进行提示
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-30 13:38
 * @since 0.0.1-SNAPSHOT
 */

@ConfigurationProperties(prefix = "web.security")
public class SecurityProperties {

    private final Url url = new Url();

    private final Jwt jwt = new Jwt();

    private final Auth auth = new Auth();

    private final Csrf csrf = new Csrf();

    public Url getUrl() { return this.url; }

    public Jwt getJwt() {
        return jwt;
    }

    public Auth getAuth() {
        return auth;
    }

    public Csrf getCsrf() {
        return csrf;
    }

    public static class Url {
        private String loginPath = "/login";

        private String loginErrorPath = "/login?error";

        private String logoutPath = "/logout";

        private String registerPath = "/register";

        private String errorPath = "/error";

        private String indexPath = "/";

        /*
         * ant path
         */
        private String[] excludePath = new String[]{};

        public String getLoginPath() {
            return loginPath;
        }

        public void setLoginPath(String loginPath) {
            this.loginPath = loginPath;
        }

        public String getLogoutPath() {
            return logoutPath;
        }

        public void setLogoutPath(String logoutPath) {
            this.logoutPath = logoutPath;
        }

        public String getErrorPath() {
            return errorPath;
        }

        public void setErrorPath(String errorPath) {
            this.errorPath = errorPath;
        }

        public String getRegisterPath() {
            return registerPath;
        }

        public void setRegisterPath(String registerPath) {
            this.registerPath = registerPath;
        }

        public String getIndexPath() {
            return indexPath;
        }

        public void setIndexPath(String indexPath) {
            this.indexPath = indexPath;
        }

        public String[] getExcludePath() {
            return excludePath;
        }

        public void setExcludePath(String[] excludePath) {
            this.excludePath = excludePath;
        }

        public String getLoginErrorPath() {
            return loginErrorPath;
        }

        public void setLoginErrorPath(String loginErrorPath) {
            this.loginErrorPath = loginErrorPath;
        }
    }

    public static class Jwt {

        private String key;

        /*
         * 最大有效时长
        */
        private long maxAge = 1800;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        /**
         * 秒
         */
        public long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }

    public static class Auth{
        private String cookieName = "ACCESS-TOKEN";

        private String headerName = "Authorization";

        private String successUrl = "/";

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String tokenName) {
            this.cookieName = tokenName;
        }

        public String getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(String successUrl) {
            this.successUrl = successUrl;
        }

    }

    public static class Csrf{

        private String headerName = "X-Xsrf-Token";

        private String cookieName = "XSRF-TOKEN";

        private String parameterName = "_csrf";

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String cookieName) {
            this.cookieName = cookieName;
        }

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }
    }


}
