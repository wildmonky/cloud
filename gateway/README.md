# swagger 访问地址
    /swagger.html ===> /webjars/swagger-ui/index.html


# 路由存储
* <b>redis</b><br/>
***注意 路由通过redis存储时，使用自定义的ObjectMapper，
使用org.springframework.cloud.gateway.config.GatewayRedisAutoConfiguration#reactiveRedisRouteDefinitionTemplate初始化时，使用new ObjectMapper()配置***