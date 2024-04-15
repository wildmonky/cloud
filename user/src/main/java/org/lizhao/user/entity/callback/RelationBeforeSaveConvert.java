package org.lizhao.user.entity.callback;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.relation.Relation;
import org.lizhao.base.model.UserInfoHolder;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

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
        return Mono.just(UserInfoHolder.get())
                .switchIfEmpty(Mono.error(new Throwable("未获取到用户信息")))
                .handle((userInfo, sink) -> {
                    String currentUserId = userInfo.getId();
                    String currentUsername = userInfo.getName();
                    try {
                        Field id = entity.getClass().getDeclaredField("id");
                        id.setAccessible(true);
                        Object o = id.get(entity);

                        if (o == null) {
                            id.set(entity, String.valueOf(idGenerator.generateNextId()));
                            entity.setCreateUseId(currentUserId);
                            entity.setCreateUseName(currentUsername);
                            entity.setCreateTime(LocalDateTime.now());
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        sink.error(new RuntimeException(e));
                        return;
                    }
                    sink.next(entity);
                });
    }
}
