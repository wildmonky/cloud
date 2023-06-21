package org.lizhao.cloud.gateway.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Description route service 路由服务接口
 * spring-cloud-gateway 使用 {@link CachingRouteLocator} 来进行路由的缓存与刷新，但不能新增、修改
 * 注意，发布 {@link RefreshRoutesEvent} 时，要加上元数据MetaData,
 * {@link RefreshRoutesEvent} 通过MetaData来刷新路由缓存 {@link CachingRouteLocator#onApplicationEvent(RefreshRoutesEvent)}
 *
 * 🤔考虑内存问题，但是路由数据占用空间是很小的，暂时不考虑
 * 替换路由定义类，实现路由的保存时，参考{@link RedisRouteDefinitionRepository}
 *
 * @see RedisRouteDefinitionRepository 路由定义存储 😎 更新路由定义后，要发布{@link RefreshRoutesEvent}路由刷新事件,{@link CachingRouteLocator}会监听事件并更新路由缓存
 * @see RouteDefinitionRouteLocator 从RouteDefinitionLocator加载Route {@link GatewayAutoConfiguration#routeDefinitionRouteLocator}
 * @see GatewayAutoConfiguration#cachedCompositeRouteLocator 将加载的RouteLocator使用缓存管理，在缓存中找不到时，再从{@link RouteDefinitionRouteLocator}获取Route
 * @see CachingRouteLocator#onApplicationEvent(RefreshRoutesEvent)
 * @see RoutePredicateHandlerMapping getHandlerInternal，lookupRoute 针对request，通过{@link RouteLocator}判断是否转发
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 17:45
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public abstract class RouteService {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Description 刷新路由 路由修改后必须刷新生效 👿🐱‍👤
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:40
     */
    public void refresh() {
        log.info("{},{}","触发路由刷新！！！", this);
        // 刷新缓存路由 CachingRouteLocator#onApplicationEvent(RefreshRoutesEvent)
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
