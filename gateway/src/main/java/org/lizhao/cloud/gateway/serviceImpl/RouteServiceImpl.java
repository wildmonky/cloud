package org.lizhao.cloud.gateway.serviceImpl;

import org.lizhao.cloud.base.gateway.service.RouteService;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 路由服务接口实现类
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 17:47
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class RouteServiceImpl implements RouteService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    public List<Route> routeList() {
        //TODO 获取路由列表信息
        return null;
    }

    /**
     * Description 保存路由
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:38
     * @param route route
     */
    public void saveRoute(RouteDefinition route) {
        routeDefinitionWriter.save(Mono.just(route)).subscribe();
        this.refresh();
    }

    /**
     * Description 删除路由
     *
     * @since 1.8.0
     * @author lizhao
     * @date 2022/6/5 19:40
     * @param route route
     */
    public void deleteRoute(RouteDefinition route) {
        routeDefinitionWriter.delete(Mono.just(route.getId()));
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
        applicationContext.publishEvent(new RefreshRoutesEvent(this));
    }

}
