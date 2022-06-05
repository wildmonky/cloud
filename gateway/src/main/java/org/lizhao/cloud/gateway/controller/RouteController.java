package org.lizhao.cloud.gateway.controller;

import org.lizhao.cloud.base.gateway.service.RouteService;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Description 网关路由配置
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 16:19
 * @since 0.0.1-SNAPSHOT
 */
@Controller
@RequestMapping("gateway")
public class RouteController {

    @Resource
    private RouteService routeService;

    @GetMapping( path = "routeList", produces = "application/json" )
    public Mono<Route> routeList() {
        return null;
    }

}
