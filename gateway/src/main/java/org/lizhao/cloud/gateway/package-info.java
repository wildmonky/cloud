/**
 *gateway路由适配机制：
 *     {@linkplain org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping} 中的lookupRoute方法中遍历RouteLocator的route，根据route中配置的Predicate
 * 来决定是否使用该路由，{@linkplain org.springframework.cloud.gateway.route.CachingRouteLocator}中对RefreshRoutesEvent进行了监听，会对缓存的Route进行刷新，其实就是，
 * 先删除缓存的Route（因为使用的是Map保存，所以直接set就可以了），再从RouteDefinitionLocator读取配置的RouteDefinition，最后进行缓存。
 *
 * 所以在刷新路由时，就是先修改路由定义即RouteDefinition再发布RefreshRoutesEvent，触发路由缓存刷新。
 * 但是当网关多实例时，其他网关就不会刷新了，这是可以使用MessageQueue来进行事件的发布，从而使变更后的路由再其他网关生效。
 *
 * 路由存储
 * 1、内存 InMemory
 * 2、redis {@link org.springframework.cloud.gateway.config.GatewayRedisAutoConfiguration#reactiveRedisRouteDefinitionTemplate}
 *
 *
 * @see org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping
 * @see org.springframework.cloud.gateway.route.CachingRouteLocator
 * @see org.springframework.cloud.gateway.event.RefreshRoutesEvent
 */
package org.lizhao.cloud.gateway;