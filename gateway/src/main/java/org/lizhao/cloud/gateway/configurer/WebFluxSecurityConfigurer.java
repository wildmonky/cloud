package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.lizhao.cloud.gateway.handler.ResourceHandler;
import org.lizhao.cloud.gateway.security.XMLHttpRequestRedirectStrategy;
import org.lizhao.cloud.gateway.security.authentication.ExchangeReactiveAuthorizationManager;
import org.lizhao.cloud.gateway.security.authentication.OutsideReactiveAuthenticationManager;
import org.lizhao.cloud.gateway.security.authentication.TokenAuthenticationToken;
import org.lizhao.cloud.gateway.security.authentication.handler.SwitchAutoRedirectAuthenticationSuccessHandler;
import org.lizhao.cloud.gateway.security.authentication.handler.LogAndRedirectAuthenticationFailureHandler;
import org.lizhao.cloud.gateway.security.context.repository.RedisSecurityContextRepository;
import org.lizhao.cloud.gateway.security.log.handler.RedisLogoutHandler;
import org.lizhao.cloud.gateway.security.log.handler.RedisLogoutSuccessHandler;
import org.lizhao.cloud.gateway.security.userdetailsservice.DelegateReactiveUserDetailsServiceImpl;
import org.lizhao.cloud.gateway.security.userdetailsservice.WebClientUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description 网关安全配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2023-07-10 22:10
 * @since 0.0.1-SNAPSHOT
 * @see org.springframework.security.config.annotation.web.reactive.ServerHttpSecurityConfiguration#authenticationManager() 根据UserDetailSerivce配置认证管理器
 * @see SecurityWebFiltersOrder WebFilter 优先级
 * @see UserDetailsRepositoryReactiveAuthenticationManager
 * @see AbstractUserDetailsReactiveAuthenticationManager
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager = true)
public class WebFluxSecurityConfigurer {

    @Resource
    private org.lizhao.cloud.gateway.configurer.properties.SecurityProperties securityProperties;

    @Bean
    public CookieServerCsrfTokenRepository cookieServerCsrfTokenRepository() {
        CookieServerCsrfTokenRepository csrfTokenRepository = new CookieServerCsrfTokenRepository();
        csrfTokenRepository.setCookieName(securityProperties.getCsrf().getCookieName());
        csrfTokenRepository.setHeaderName(securityProperties.getCsrf().getHeaderName());
        csrfTokenRepository.setParameterName(securityProperties.getCsrf().getParameterName());
        csrfTokenRepository.setCookieCustomizer(csrf -> {
            csrf.httpOnly(false);
            csrf.maxAge(securityProperties.getJwt().getMaxAge());
        });
        return csrfTokenRepository;
    }

    @Bean
    public RedisSecurityContextRepository redisSecurityContextRepository(org.lizhao.cloud.gateway.configurer.properties.SecurityProperties securityProperties,
                                                                         ReactiveRedisTemplate<String, TokenAuthenticationToken> reactiveTokenRedisTemplate) {
        return new RedisSecurityContextRepository(securityProperties, reactiveTokenRedisTemplate);
    }

    @Bean
    public RedisLogoutHandler redisLogoutHandler(RedisSecurityContextRepository repository) {
        return new RedisLogoutHandler(repository);
    }

    @Bean
    public ServerAuthenticationEntryPoint redirectServerAuthenticationEntryPoint() {
        RedirectServerAuthenticationEntryPoint authenticationEntryPoint = new RedirectServerAuthenticationEntryPoint(securityProperties.getUrl().getLoginPath());
        authenticationEntryPoint.setRedirectStrategy(new XMLHttpRequestRedirectStrategy());
        return authenticationEntryPoint;
    }

    @Bean
    public RedisLogoutSuccessHandler redisLogoutSuccessHandler() {
        return new RedisLogoutSuccessHandler();
    }

    @Bean
    public ExchangeReactiveAuthorizationManager exchangeReactiveAuthorizationManager(ResourceHandler resourceHandler,
                                                                                     WebClientUserDetailsServiceImpl webClientUserDetailsServiceImpl) {
        return new ExchangeReactiveAuthorizationManager(resourceHandler, webClientUserDetailsServiceImpl);
    }

    /**
     * reactive security !!!
     * @see ServerAuthenticationFailureHandler 认证成功处理器
     * @see LoginPageGeneratingWebFilter 登录页面生成
     * @see AuthenticationWebFilter 认证过滤器
     * @see AuthenticationWebFilter#onAuthenticationSuccess(Authentication, WebFilterExchange) 会设置SecurityContext
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            RedisLogoutHandler userLogoutHandler,
                                                            RedisLogoutSuccessHandler userLogoutSuccessHandler,
                                                            RedisSecurityContextRepository redisSecurityContextRepository,
                                                            CookieServerCsrfTokenRepository cookieServerCsrfTokenRepository,
                                                            ServerAuthenticationEntryPoint redirectServerAuthenticationEntryPoint,
                                                            DelegateReactiveUserDetailsServiceImpl delegateReactiveUserDetailsService,
                                                            ObjectMapper objectMapper,
                                                            ExchangeReactiveAuthorizationManager exchangeReactiveAuthorizationManager
    ) {
        // 注意 登录地址、注册地址都不需要csrf、auth
        http.headers(headerSpec ->
                    // 允许同域名下的 frame
                    headerSpec.frameOptions(frameOptionsSpec ->
                        frameOptionsSpec.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)
                    )
                )
                // SecurityContext(包含用户信息) redis保存策略
                .securityContextRepository(redisSecurityContextRepository)
//                .httpBasic(Customizer.withDefaults())
                // 允许跨域请求
                .cors(corsSpec -> {
                    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOrigin("*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedMethod("*");
                    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
                    corsSpec.configurationSource(urlBasedCorsConfigurationSource);
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .csrf(csrfSpec -> csrfSpec.accessDeniedHandler(new CsrfServerAccessDeniedHandler())
//                        // client: cookie中保存 XSRF-TOKEN  server: 验证请求的头 X-XSRF-TOKEN
//                        .csrfTokenRepository(cookieServerCsrfTokenRepository)
//                        // 排除 登录和注册 页面
////                        .requireCsrfProtectionMatcher(new CsrfRequestMatcher())
//                        .csrfTokenRequestHandler(new ServerCsrfTokenRequestAttributeHandler())
//                )
                // 表单验证
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .formLogin(formLogin ->
                    formLogin.authenticationManager(new OutsideReactiveAuthenticationManager(delegateReactiveUserDetailsService))
                            // 如果不配置AuthenticationEntryPoint，会自动创建 LoginPageGeneratingWebFilter
                            .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
//                            .authenticationEntryPoint(redirectServerAuthenticationEntryPoint)
                            // 配置需要进行 authentication 的请求Url，AuthenticationWebFilter
                            .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, securityProperties.getUrl().getLoginPath()))
                            // AuthenticationWebFilter
                            .authenticationSuccessHandler(SwitchAutoRedirectAuthenticationSuccessHandler.create(objectMapper, securityProperties.getUrl().getIndexPath()))
                            .authenticationFailureHandler(new LogAndRedirectAuthenticationFailureHandler(securityProperties.getUrl().getLoginErrorPath()))
                            // redirect，自动配置了 AuthenticationEntryPoint、AuthenticationWebFilter、entrypoint、failureHandler，failureHandler跳转到 {loginPage}?error
//                           .loginPage("/gate/login")
                )
                .logout(logoutSpec ->
                    logoutSpec
//                            .logoutUrl("/logout") // 使用 POST 方法
                            .logoutHandler(userLogoutHandler)
                            .logoutSuccessHandler(userLogoutSuccessHandler)
                            .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                )
                .authorizeExchange(exchange -> {
                    // 登录页面 无需验证
                    List<String> excludePath = new ArrayList<>(Arrays.asList(securityProperties.getUrl().getExcludePath()));
                    excludePath.add(securityProperties.getUrl().getLoginPath());
                    excludePath.add(securityProperties.getUrl().getLoginErrorPath());
                    excludePath.add(securityProperties.getUrl().getRegisterPath());

                    exchange.pathMatchers(HttpMethod.GET,
                                    excludePath.toArray(new String[0]))
                            .permitAll()
//                            .anyExchange()
//                            .authenticated()
                            .anyExchange()
                            .access(exchangeReactiveAuthorizationManager);
                });
        // for test
//        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll())
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .logout(ServerHttpSecurity.LogoutSpec::disable)
//                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebClientUserDetailsServiceImpl webClientUserDetailsService(WebClient userServiceWebClient) {
        return new WebClientUserDetailsServiceImpl(userServiceWebClient);
    }


    /**
     * redis jwt template
     * @param factory
     */
    @Bean
    public ReactiveRedisTemplate<String, TokenAuthenticationToken> reactiveTokenRedisTemplate(ReactiveRedisConnectionFactory factory, ObjectMapper objectMapper) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<TokenAuthenticationToken> valueSerializer = new Jackson2JsonRedisSerializer<>(
                objectMapper, TokenAuthenticationToken.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, TokenAuthenticationToken> builder = RedisSerializationContext
                .newSerializationContext(keySerializer);
        RedisSerializationContext<String, TokenAuthenticationToken> context = builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    @Primary
    public DelegateReactiveUserDetailsServiceImpl reactiveUserDetailsService(WebClientUserDetailsServiceImpl webClientUserDetailsServiceImpl) {
        return new DelegateReactiveUserDetailsServiceImpl(webClientUserDetailsServiceImpl);
    }

}
