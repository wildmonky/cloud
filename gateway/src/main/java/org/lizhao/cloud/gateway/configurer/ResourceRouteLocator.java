package org.lizhao.cloud.gateway.configurer;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.enums.ResourceTypeEnum;
import org.lizhao.base.model.TreeNode;
import org.lizhao.base.model.resource.ServerResourceModel;
import org.lizhao.cloud.gateway.filter.gateway.StripPrefixGatewayFilter;
import org.lizhao.cloud.gateway.handler.ResourceHandler;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description 将项目类型资源加载为路由
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-27 18:40
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class ResourceRouteLocator implements RouteLocator {

    @Resource
    private ResourceHandler resourceHandler;

    @Override
    public Flux<Route> getRoutes() {
        return resourceHandler.findByType(ResourceTypeEnum.PROJECT)
                .flatMapMany(resources -> {
                    List<Route> list = new ArrayList<>();
                    for (ServerResourceModel resource : resources) {
                        parse(list, resource, resource.getActualPath());
                    }
                    return Flux.fromIterable(list);
                });
    }

    public void parse(List<Route> routeList, TreeNode<ServerResourceModel> resource, String uri) {
        ServerResourceModel model = (ServerResourceModel) resource;
        Collection<TreeNode<ServerResourceModel>> children = resource.getChildren();
        if (ObjectUtils.isNotEmpty(children)) {
            for (TreeNode<ServerResourceModel> child : children) {
                parse(routeList, child, model.getActualPath());
            }
        }

        routeList.add(
                Route.async()
                        .id(model.getId())
                        .uri(StringUtils.isBlank(model.getActualPath()) ? uri : model.getActualPath())
                        .predicate(exchange ->
                            exchange.getRequest().getPath().pathWithinApplication().value()
                                    .startsWith(model.getPath())
                        ).filter(new StripPrefixGatewayFilter(1))
                        .build()
        );
    }

}
