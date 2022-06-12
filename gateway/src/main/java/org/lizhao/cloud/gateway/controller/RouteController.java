package org.lizhao.cloud.gateway.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lizhao.cloud.gateway.serviceImpl.RouteServiceImpl;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 网关路由配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 16:19
 * @since 0.0.1-SNAPSHOT
 */
@Api("路由操作")
@Controller
@RequestMapping( path = "gateway", consumes = {"application/json", "application/form-data"})
public class RouteController {

    @Resource
    private RouteServiceImpl routeServiceImpl;


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
    @ApiOperation("查询路由列表")
    @GetMapping( path = "routeList", produces = "application/json" )
    public Flux<RouteDefinition> routeList(@RequestParam String id,
                                 @RequestParam String source,
                                 @RequestParam String target
    ) {
        return routeServiceImpl.routeList(id, source, target);
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
    @ApiOperation("更新、新增路由")
    @PutMapping( path = "routeList", produces = "text/plain")
    public Mono<ServerResponse> saveRouteList(@RequestBody List<RouteDefinition> routeDefinitionList) {
        routeServiceImpl.saveRouteList(routeDefinitionList);
        return ServerResponse.ok().bodyValue("路由保存成功");
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
    @ApiOperation("删除路由")
    @DeleteMapping( path = "routeList", produces = "text/plain")
    public Mono<ServerResponse> removeRouteList(List<RouteDefinition> routeDefinitionList) {
        routeServiceImpl.removeRouteList(routeDefinitionList);
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("路由删除成功");
    }

}
