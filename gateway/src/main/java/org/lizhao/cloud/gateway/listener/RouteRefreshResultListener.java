package org.lizhao.cloud.gateway.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Description 路由刷新结果监听器，提供实际的路由更新逻辑。
 *
 * 不同于 {@link org.springframework.cloud.gateway.route.RouteRefreshListener}:
 *    其当系统中出现的事件需要更新路由时，发布路由更新事件{@link org.springframework.cloud.gateway.event.RefreshRoutesEvent}
 *    并没有实际更新路由的逻辑处理。
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-05 18:57
 * @since 0.0.1-SNAPSHOT
 */
@Component
@Slf4j
public class RouteRefreshResultListener implements ApplicationListener<RefreshRoutesResultEvent> {

    @Override
    public void onApplicationEvent(RefreshRoutesResultEvent event) {
        log.info("路由刷新结果：{}", event.isSuccess() ? "成功" : "失败" );
    }
}
