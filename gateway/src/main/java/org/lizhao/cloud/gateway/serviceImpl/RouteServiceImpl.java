package org.lizhao.cloud.gateway.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.cloud.base.gateway.service.RouteService;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
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
@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;
    @Resource
    private RouteDefinitionLocator routeDefinitionLocator;

    public Flux<RouteDefinition> routeList(String id, String source, String target) {
        Flux<RouteDefinition> routeDefinitionFlux = routeDefinitionLocator.getRouteDefinitions();
        return routeDefinitionFlux.collectList().flatMapIterable(p ->
            p.stream().filter(p1 -> {
                if (StringUtils.isNotBlank(id) && !p1.getId().equalsIgnoreCase(id)) {
                    return false;
                }
                if (StringUtils.isNotBlank(source)) {
                    List<PredicateDefinition> predicates = p1.getPredicates();
                    if (ObjectUtils.isNotEmpty(predicates)) {
                        boolean sourceFlag = false;
                        for (PredicateDefinition predicate : predicates) {
                            if (predicate.getName().equalsIgnoreCase("")) {
                                predicate.getArgs();
                                sourceFlag = true;
                            }
                        }
                        if (!sourceFlag) {
                            return sourceFlag;
                        }
                    }
                }
                return !StringUtils.isNotBlank(target) || p1.getUri().getPath().contains(target);
            }).collect(Collectors.toList())
        );


        //TODO 获取路由列表信息
    }

    /**
     * Description 保存路由 更新、新增
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:38
     * @param routeDefinitionList  要保存的routeDefinition列表
     */
    public void saveRouteList(List<RouteDefinition> routeDefinitionList) {
        routeDefinitionList.forEach(routeDefinition -> routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe());
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
    public void removeRouteList(List<RouteDefinition> routeDefinitionList) {
        routeDefinitionList.forEach(routeDefinition -> routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())));;
        this.refresh();
    }

    /**
     * Description 刷新路由
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:40
     */
    @Override
    public void refresh() {
        log.info("{} {}","触发路由刷新！！！", this);
        applicationContext.publishEvent(new RefreshRoutesEvent(this));
    }

}
