package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.validation.constraints.NotNull;
import org.lizhao.cloud.gateway.repository.*;
import org.lizhao.cloud.gateway.security.security.context.repository.RedisSecurityContextRepository;
import org.lizhao.cloud.gateway.security.userdetailsservice.DelegateReactiveUserDetailsService;
import org.lizhao.cloud.gateway.security.userdetailsservice.DBReactiveUserDetailsService;
import org.lizhao.cloud.gateway.security.authentication.handler.LogAndRedirectAuthenticationFailureHandler;
import org.lizhao.cloud.gateway.security.authentication.handler.CookieTokenRedirectAuthenticationSuccessHandler;
import org.lizhao.cloud.gateway.security.csrf.CsrfRequestMatcher;
import org.lizhao.cloud.gateway.security.csrf.CsrfServerAccessDeinedHandler;
import org.lizhao.cloud.gateway.security.log.handler.RedisLogoutHandler;
import org.lizhao.cloud.gateway.security.log.handler.RedisLogoutSuccessHandler;
import org.lizhao.cloud.gateway.utils.json.deserializer.FilterDefinitionDeserializer;
import org.lizhao.cloud.gateway.utils.json.deserializer.PredicateDefinitionDeserializer;
import org.lizhao.cloud.web.react.handler.GlobalResponseBodyHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.AbstractJackson2Decoder;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;

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
public class WebfluxSecurityConfigurer implements WebFluxConfigurer {

    @Bean
    public GlobalResponseBodyHandler responseWrapper(@NotNull ServerCodecConfigurer serverCodecConfigurer,
                                                     RequestedContentTypeResolver requestedContentTypeResolver) {
        return new GlobalResponseBodyHandler(serverCodecConfigurer.getWriters(), requestedContentTypeResolver);
    }

    /**
     * 添加自定义类型转换 支持类型:
     *      1、Decoder, Encoder;
     *      2、HttpMessageReader, HttpMessageWriter;
     *
     * @see DefaultServerCodecConfigurer
     * org.springframework.http.codec.support.BaseCodecConfigurer的实现类
     * 从其getReaders()方法中可以看出自定义的优先级最高 {@link DefaultServerCodecConfigurer#getReaders()}
     * @param configurer the configurer to customize readers and writers
     */
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        //设置自定义request body 类型转换
//        configurer.customCodecs().register(new RouteDefinitionDecoder());
        configurer.defaultCodecs().enableLoggingRequestDetails(true);
        configurer.defaultCodecs().configureDefaultCodec(codec -> {
            if (codec instanceof AbstractJackson2Decoder abstractJackson2Decoder) {
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addDeserializer(PredicateDefinition.class, new PredicateDefinitionDeserializer());
                simpleModule.addDeserializer(FilterDefinition.class, new FilterDefinitionDeserializer());
                abstractJackson2Decoder.getObjectMapper().registerModule(simpleModule);
            }
        });
    }

    /**
     * reactive security !!!
     * @param http
     * @see ServerAuthenticationFailureHandler 认证成功处理器
     * @see LoginPageGeneratingWebFilter 登录页面生成
     * @see AuthenticationWebFilter 认证过滤器
     * @see AuthenticationWebFilter#onAuthenticationSuccess(Authentication, WebFilterExchange) 会设置SecurityContext
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            RedisLogoutHandler userLogoutHandler,
                                                            RedisLogoutSuccessHandler userLogoutSuccessHandler,
                                                            RedisSecurityContextRepository redisSecurityContextRepository
    ) {
        // 注意 登录地址、注册地址都不需要csrf、auth
        // SecurityContext(包含用户信息) redis保存策略
        http.securityContextRepository(redisSecurityContextRepository)
                .authorizeExchange(exchange ->
                        // 登录页面 无需验证
                    exchange.pathMatchers(HttpMethod.GET, "/login", "/register")
                            .permitAll()
                            .anyExchange().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                // 允许跨域请求
                .cors(corsSpec -> corsSpec.configurationSource(new UrlBasedCorsConfigurationSource()))
                .csrf(csrfSpec -> csrfSpec.accessDeniedHandler(new CsrfServerAccessDeinedHandler())
                        // client: cookie中保存 XSRF-TOKEN  server: 验证请求的头 X-XSRF-TOKEN
                        .csrfTokenRepository(new CookieServerCsrfTokenRepository())
                        // 排除 登录和注册 页面
                        .requireCsrfProtectionMatcher(CsrfRequestMatcher.exclude("/login", "/register"))
                        .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler())
                ).formLogin(formLogin -> // 表单验证
                    formLogin
                            // 如果不配置AuthenticationEntryPoint，会自动创建 LoginPageGeneratingWebFilter
                            .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/login"))
                            // 配置需要进行 authentication 的请求Url，AuthenticationWebFilter
                            .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login"))
                            // AuthenticationWebFilter
                            .authenticationSuccessHandler(CookieTokenRedirectAuthenticationSuccessHandler.create("TOKEN","/"))
                            .authenticationFailureHandler(new LogAndRedirectAuthenticationFailureHandler("/login?error"))
                            // redirect，自动配置了 AuthenticationEntryPoint、AuthenticationWebFilter、entrypoint、failureHandler，failureHandler跳转到 {loginPage}?error
//                           .loginPage("/gate/login")
                ).logout(logoutSpec ->
                    logoutSpec
//                            .logoutUrl("/logout") // 使用 POST 方法
                            .logoutHandler(userLogoutHandler)
                            .logoutSuccessHandler(userLogoutSuccessHandler)
                            .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DBReactiveUserDetailsService reactiveDBUserDetailsService(SecurityProperties properties,
                                                                     ObjectProvider<PasswordEncoder> passwordEncoder,
                                                                     UserRepository userRepository,
                                                                     RoleRepository roleRepository,
                                                                     GroupRepository groupRepository,
                                                                     AuthorityRepository authorityRepository,
                                                                     GroupUserRelationRepository groupUserRelationRepository,
                                                                     GroupAuthorityRelationRepository groupAuthorityRelationRepository,
                                                                     UserRoleRelationRepository userRoleRelationRepository,
                                                                     UserAuthorityRelationRepository userAuthorityRelationRepository,
                                                                     RoleAuthorityRelationRepository roleAuthorityRelationRepository
    ) {
        return new DBReactiveUserDetailsService(properties, passwordEncoder,
                userRepository,
                roleRepository,
                groupRepository,
                authorityRepository,
                groupUserRelationRepository,
                groupAuthorityRelationRepository,
                userRoleRelationRepository,
                userAuthorityRelationRepository,
                roleAuthorityRelationRepository
        );
    }


    /**
     * redis jwt template
     * @param factory
     * @return
     */
    @Bean
    public ReactiveRedisTemplate<String, Authentication> reactiveTokenRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Authentication> valueSerializer = new Jackson2JsonRedisSerializer<>(
                Authentication.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Authentication> builder = RedisSerializationContext
                .newSerializationContext(keySerializer);
        RedisSerializationContext<String, Authentication> context = builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    @Primary
    public DelegateReactiveUserDetailsService reactiveUserDetailsService(DBReactiveUserDetailsService dbReactiveUserDetailsService) {
        return new DelegateReactiveUserDetailsService(dbReactiveUserDetailsService);
    }

}
