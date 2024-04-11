* 测试前，关闭spring security
```
http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll())
               .csrf(ServerHttpSecurity.CsrfSpec::disable)
               .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
               .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
               .logout(ServerHttpSecurity.LogoutSpec::disable);
```
> 注意：  @WebFluxTest 只会加载 WebFluxConfigurer 配置类，以及一些必须的实体，所以要考虑上述代码是否在WebFluxConfigurer 配置类中，
如果不在，可以使用@Import直接导入配置类