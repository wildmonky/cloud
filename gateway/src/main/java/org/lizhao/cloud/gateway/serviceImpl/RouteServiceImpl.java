package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.lizhao.cloud.gateway.service.RouteService;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description 路由服务接口实现类
 *
 * routerDefinition json
 * {
 *     id:
 *     url:
 *     order:
 *     metadata: [{
 *          key: '',
 *          value: ''
 *     }],
 *     predicates: [{
 *         name: '',
 *         args: [{
 *              key: '',
 *              value: ''
 *         }]
 *     }],
 *     filters: [{
 *         name: '',
 *         args: [{
 *              key: '',
 *              value: ''
 *         }]
 *     }]
 * }
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 17:47
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
public class RouteServiceImpl extends RouteService {
    @Resource
    private RedisRouteDefinitionRepository redisRouteDefinitionRepository;

    public Mono<List<RouteDefinition>> search(String id, String source, String target) {
        Flux<RouteDefinition> routeDefinitionFlux = redisRouteDefinitionRepository.getRouteDefinitions();
        return routeDefinitionFlux.filter(route -> {
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
        }).collectList();
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
                redisRouteDefinitionRepository.delete(Mono.just(routeDefinition.getId())).subscribe());
        this.refresh();
    }

}
