package org.lizhao.cloud.gateway.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.lizhao.cloud.gateway.json.deserializer.RouteDefinitionDeserializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Description 自定义 RouteDefinition 反序列化
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-14 20:56
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class GatewayBeanPostProcessor implements BeanPostProcessor {

    /**
     * 自定义 RouteDefinition 反序列化，替换{@link org.springframework.cloud.gateway.config.GatewayRedisAutoConfiguration#reactiveRedisRouteDefinitionTemplate}
     *
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("reactiveRedisRouteDefinitionTemplate".equals(beanName)) {
            ObjectMapper om = new ObjectMapper();
            SimpleModule sm = new SimpleModule();
            sm.addDeserializer(RouteDefinition.class, new RouteDefinitionDeserializer());
            om.registerModule(sm);

            ReactiveRedisTemplate<String, RouteDefinition> redisRouteDefinitionTemplate = (ReactiveRedisTemplate<String, RouteDefinition>) bean;

            StringRedisSerializer keySerializer = new StringRedisSerializer();
            Jackson2JsonRedisSerializer<RouteDefinition> valueSerializer = new Jackson2JsonRedisSerializer<>(
                    om, RouteDefinition.class);
            RedisSerializationContext.RedisSerializationContextBuilder<String, RouteDefinition> builder = RedisSerializationContext
                    .newSerializationContext(keySerializer);
            RedisSerializationContext<String, RouteDefinition> context = builder.value(valueSerializer).build();

            bean = new ReactiveRedisTemplate<>(redisRouteDefinitionTemplate.getConnectionFactory(), context);
        }

        return bean;
    }

}
