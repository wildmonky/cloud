package org.lizhao.cloud.gateway.entity.callback;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.Relation;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Description relation r2dbc保存前回调
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 18:55
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class RelationBeforeSaveConvert implements BeforeConvertCallback<Relation> {

    @Resource
    private SnowFlake idGenerator;

    @Override
    public Publisher<Relation> onBeforeConvert(Relation entity, SqlIdentifier table) {
        if (entity == null) {
            return Mono.just(entity);
        }
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new Throwable("上下文为空")))
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    Object principal = authentication.getPrincipal();
                    Assert.notNull(principal, "未获取到用户信息");
//                    Assert.isInstanceOf(GatewayUser.class, principal, () -> "安全上下文异常");
                    // TODO 考虑优化
                    Map<String, Object> currentUser = (HashMap<String, Object>) principal;
                    try {
                        Field id = entity.getClass().getDeclaredField("id");
                        id.setAccessible(true);
                        Object o = id.get(entity);
                        String currentUserId = (String)currentUser.get("id");
                        String currentUsername = (String)currentUser.get("username");

                        if (o == null) {
                            id.set(entity, String.valueOf(idGenerator.generateNextId()));
                            entity.setCreateUseId(currentUserId);
                            entity.setCreateUseName(currentUsername);
                            entity.setCreateTime(LocalDateTime.now());
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return entity;
                });
    }
}
