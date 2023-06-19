package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.cloud.gateway.service.RouteService;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description 路由服务接口实现类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 17:47
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
public class RouteServiceImpl implements RouteService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private RedisRouteDefinitionRepository redisRouteDefinitionRepository;

    public Flux<RouteDefinition> search(String id, String source, String target) {
        Flux<RouteDefinition> routeDefinitionFlux = redisRouteDefinitionRepository.getRouteDefinitions();
        return routeDefinitionFlux.collectList().flatMapIterable(routeList ->
            routeList.stream().filter(route -> {
                if (StringUtils.isNotBlank(id) && !route.getId().equalsIgnoreCase(id)) {
                    return false;
                }
                if (StringUtils.isNotBlank(source)) {
                    List<PredicateDefinition> predicates = route.getPredicates();
                    if (ObjectUtils.isNotEmpty(predicates)) {
                        boolean sourceFlag = false;
                        for (PredicateDefinition predicate : predicates) {
                            if (StringUtils.isBlank(predicate.getName())) {
//                                predicate.getArgs();
                                sourceFlag = true;
                            }
                        }

                        if (!sourceFlag) {
                            return false;
                        }
                    }
                }
                return !StringUtils.isNotBlank(target) || route.getUri().getPath().contains(target);
            }).collect(Collectors.toList())
        );
    }

    /**
     * Description 保存路由 更新、新增
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:38
     * @param routeDefinitionList  要保存的routeDefinition列表
     */
    public void batchSave(List<RouteDefinition> routeDefinitionList) {
        routeDefinitionList.forEach(routeDefinition -> redisRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe());
        this.refresh();
    }

    /**
     * Description 删除路由
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:40
     * @param routeDefinitionList 要删除的routeDefinition列表
     */
    public void batchRemove(List<RouteDefinition> routeDefinitionList) {
        routeDefinitionList.forEach(routeDefinition ->
                redisRouteDefinitionRepository.delete(Mono.just(routeDefinition.getId())));
        this.refresh();
    }

    /**
     * Description 刷新路由
     *
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
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:40
     */
    @Override
    public void refresh() {
        log.info("{},{}","触发路由刷新！！！", this);
        // 刷新缓存路由 CachingRouteLocator#onApplicationEvent(RefreshRoutesEvent)
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
