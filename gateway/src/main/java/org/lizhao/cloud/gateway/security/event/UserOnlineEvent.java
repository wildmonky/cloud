package org.lizhao.cloud.gateway.security.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Description 用户上线事件
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-01 13:53
 * @since 0.0.1-SNAPSHOT
 */
public class UserOnlineEvent extends ApplicationEvent {
    public UserOnlineEvent(Object source) {
        super(source);
    }

    public UserOnlineEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
