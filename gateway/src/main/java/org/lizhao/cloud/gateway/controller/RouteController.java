package org.lizhao.cloud.gateway.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.lizhao.cloud.gateway.serviceImpl.RouteServiceImpl;
import org.springframework.cloud.gateway.actuate.AbstractGatewayControllerEndpoint;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description 网关路由配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 16:19
 * @since 0.0.1-SNAPSHOT
 */
@Tag(name = "路由操作")
@RequestMapping(path = "/route", consumes = {"application/json", "application/form-data"})
@Controller
public class RouteController extends AbstractGatewayControllerEndpoint {

    @Resource
    private RouteServiceImpl routeServiceImpl;

    public RouteController(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters, List<GatewayFilterFactory> gatewayFilters, List<RoutePredicateFactory> routePredicates, RouteDefinitionWriter routeDefinitionWriter, RouteLocator routeLocator) {
        super(routeDefinitionLocator, globalFilters, gatewayFilters, routePredicates, routeDefinitionWriter, routeLocator);
    }


    /**
     * Description 查询路由列表
     *
     * @since 0.0.1-SNAPSHOT
     * @author lizhao
     * @date 2022/6/12 18:51
     * @param id springCloud Gateway 路由配置中的Id
     * @param source source springCloud Gateway 路由配置中的适配路由，即predicate中的PathRoutePredicate中配置的路由适用路径
     * @param target target springCloud Gateway 路由配置中的目标地址
     * @return reactor.core.publisher.Flux<org.springframework.cloud.gateway.route.Route>
     */
    @Operation(summary = "查询路由列表")
    @GetMapping(path = "/list", produces = "application/json" )
    @ResponseBody
    public Mono<List<RouteDefinition>> routeList(@RequestParam(required = false) String id,
                                 @RequestParam(required = false) String source,
                                 @RequestParam(required = false) String target
    ) {
        return routeServiceImpl.search(id, source, target);
    }

    /**
     * Description 更新、新增路由
     *
     * @since 0.0.1-SNAPSHOT
     * @author lizhao
     * @date 2022/6/12 18:24
     * @param routeDefinitionList routeDefinitionList
     * @return reactor.core.publisher.Mono<org.springframework.web.reactive.function.server.ServerResponse>
     */
    @Operation(summary = "更新、新增路由")
    @PostMapping(path = "save")
    @ResponseBody
    public void saveRouteList(@RequestBody List<RouteDefinition> routeDefinitionFlux) {
        routeServiceImpl.batchSave(routeDefinitionFlux);
    }

    /**
     * Description 删除路由
     *
     * @since 0.0.1-SNAPSHOT
     * @author lizhao
     * @date 2022/6/12 18:24
     * @param routeDefinitionList routeDefinitionList
     * @return reactor.core.publisher.Mono<org.springframework.web.reactive.function.server.ServerResponse>
     */
    @Operation(summary = "删除路由")
    @GetMapping(path = "/remove", produces = "text/plain")
    public Mono<ServerResponse> removeRouteList(List<RouteDefinition> routeDefinitionList) {
        routeServiceImpl.batchRemove(routeDefinitionList);
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("路由删除成功");
    }

    @Operation(summary = "查询路由列表")
    @GetMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String listTest() {
        return Mono.just("测试成功").block();
    }

}
