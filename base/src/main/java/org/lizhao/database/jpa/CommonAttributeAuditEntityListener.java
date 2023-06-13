package org.lizhao.database.jpa;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.exception.CustomException;
import org.lizhao.base.model.UserInfoHolder;

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

        if (entity instanceof CommonAttribute) {
            User currentUser = Optional.ofNullable(UserInfoHolder.getCurrentUser()).orElseThrow(() -> CustomException.NOT_LOGIN);

            CommonAttribute commonAttribute = (CommonAttribute) entity;
            commonAttribute.setCreateUserId(currentUser.getCreateUserId());
            commonAttribute.setCreateUserName(currentUser.getCreateUserName());
            commonAttribute.setCreateTime(LocalDateTime.now());
        }

    }

    @PreUpdate
    public void postUpdate(Object entity) throws CustomException {

        if (entity instanceof CommonAttribute) {
            User currentUser = Optional.ofNullable(UserInfoHolder.getCurrentUser()).orElseThrow(() -> CustomException.NOT_LOGIN);

            CommonAttribute commonAttribute = (CommonAttribute) entity;
            commonAttribute.setUpdateUserId(currentUser.getCreateUserId());
            commonAttribute.setUpdateUserName(currentUser.getCreateUserName());
            commonAttribute.setUpdateTime(LocalDateTime.now());
        }

    }

}
