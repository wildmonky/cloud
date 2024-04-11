package org.lizhao.cloud.gateway.entity.callback;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.lizhao.cloud.gateway.model.GatewayUser;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-09 22:56
 * @since 0.0.1-SNAPSHOT
 */
//@Component
public class GroupBeforeSaveConvert implements BeforeConvertCallback<Group> {

    @Resource
    private SnowFlake idGenerator;

    @Override
    public Publisher<Group> onBeforeConvert(Group entity, SqlIdentifier table) {
        if (entity == null) {
            return Mono.just(entity);
        }
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new Throwable("上下文为空")))
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    Object principal = authentication.getPrincipal();
                    Assert.notNull(principal, "未获取到用户信息");
                    Assert.isInstanceOf(GatewayUser.class, principal, () -> "安全上下文异常");
                    GatewayUser currentUser = (GatewayUser) principal;
                    if (entity.getId() == null) {
                        entity.setId(String.valueOf(idGenerator.generateNextId()));
                        entity.setCreateUseId(currentUser.getId());
                        entity.setCreateUseName(currentUser.getUsername());
                        entity.setCreateTime(LocalDateTime.now());
                    } else {
                        entity.setUpdateUseId(currentUser.getId());
                        entity.setUpdateUseName(currentUser.getUsername());
                        entity.setUpdateTime(LocalDateTime.now());
                    }
                    return entity;
                });
    }

}
