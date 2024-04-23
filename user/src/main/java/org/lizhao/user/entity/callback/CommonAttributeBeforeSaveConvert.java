package org.lizhao.user.entity.callback;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.model.ReactiveUserInfoHolder;
import org.lizhao.base.utils.uniquekey.SnowFlake;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Description 实体通用属性设置：创建人id，创建人，创建时间，更新人id，更新人，更新时间
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-09 23:03
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class CommonAttributeBeforeSaveConvert implements BeforeConvertCallback<CommonAttribute> {

    @Resource
    private SnowFlake idGenerator;
    @Override
    public Publisher<CommonAttribute> onBeforeConvert(CommonAttribute entity, SqlIdentifier table) {
        return ReactiveUserInfoHolder.get()
                .switchIfEmpty(Mono.error(new Throwable("未获取到用户信息")))
                .handle((userInfo, sink) -> {
                    String currentUserId = userInfo.getId();
                    String currentUsername = userInfo.getName();
                    boolean isNew = false;
                    try {
                        Field id = entity.getClass().getDeclaredField("id");
                        id.setAccessible(true);
                        Object o = id.get(entity);

                        if (o == null) {
                            isNew = true;
                            id.set(entity, String.valueOf(idGenerator.generateNextId()));
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        sink.error(new RuntimeException(e));
                        return;
                    }

                    if (isNew) {
                        entity.setCreateUseId(currentUserId);
                        entity.setCreateUseName(currentUsername);
                        entity.setCreateTime(LocalDateTime.now());
                    } else {
                        entity.setUpdateUseId(currentUserId);
                        entity.setUpdateUseName(currentUsername);
                        entity.setUpdateTime(LocalDateTime.now());
                    }
                    sink.next(entity);
        });
    }

}
