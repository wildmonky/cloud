package org.lizhao.database.jpa;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.exception.CustomException;
import org.lizhao.base.model.UserHolder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Description 通用实体属性设置 监听器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-10-02 15:16
 * @since 0.0.1-SNAPSHOT
 */
public class CommonAttributeAuditEntityListener {

    @PrePersist
    public void prePersist(Object entity) throws CustomException {

        if (entity instanceof CommonAttribute commonAttribute) {
            User currentUser = Optional.ofNullable(UserHolder.getCurrentUser()).orElseThrow(() -> CustomException.NOT_LOGIN);

            commonAttribute.setCreateUseId(currentUser.getCreateUseId());
            commonAttribute.setCreateUseName(currentUser.getCreateUseName());
            commonAttribute.setCreateTime(LocalDateTime.now());
        }

    }

    @PreUpdate
    public void postUpdate(Object entity) throws CustomException {

        if (entity instanceof CommonAttribute commonAttribute) {
            User currentUser = Optional.ofNullable(UserHolder.getCurrentUser()).orElseThrow(() -> CustomException.NOT_LOGIN);

            commonAttribute.setUpdateUseId(currentUser.getCreateUseId());
            commonAttribute.setUpdateUseName(currentUser.getCreateUseName());
            commonAttribute.setUpdateTime(LocalDateTime.now());
        }

    }

}
