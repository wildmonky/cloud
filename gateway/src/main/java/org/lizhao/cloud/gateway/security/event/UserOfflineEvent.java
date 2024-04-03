package org.lizhao.cloud.gateway.security.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Description 用户下线事件
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-01 13:52
 * @since 0.0.1-SNAPSHOT
 */
public class UserOfflineEvent extends ApplicationEvent {
    public UserOfflineEvent(Object source) {
        super(source);
    }

    public UserOfflineEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
