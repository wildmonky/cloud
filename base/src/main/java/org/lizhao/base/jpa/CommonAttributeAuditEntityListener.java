package org.lizhao.base.jpa;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.apache.commons.lang3.StringUtils;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.exception.CustomException;
import org.lizhao.base.model.UserInfo;
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

        if (entity instanceof CommonAttribute commonAttribute) {

            if (StringUtils.isNotBlank(commonAttribute.getCreateUseId())
                    && StringUtils.isNotBlank(commonAttribute.getCreateUseName())
                    && commonAttribute.getCreateTime() != null) {
                return;
            }

            UserInfo currentUser = Optional.ofNullable(UserInfoHolder.get()).orElseThrow(() -> CustomException.NOT_LOGIN);

            commonAttribute.setCreateUseId(currentUser.getId());
            commonAttribute.setCreateUseName(currentUser.getName());
            commonAttribute.setCreateTime(LocalDateTime.now());
        }

    }

    @PreUpdate
    public void postUpdate(Object entity) throws CustomException {

        if (entity instanceof CommonAttribute commonAttribute) {

            if (StringUtils.isNotBlank(commonAttribute.getUpdateUseId())
                    && StringUtils.isNotBlank(commonAttribute.getUpdateUseName())
                    && commonAttribute.getUpdateTime() != null) {
                return;
            }

            UserInfo currentUser = Optional.ofNullable(UserInfoHolder.get()).orElseThrow(() -> CustomException.NOT_LOGIN);

            commonAttribute.setUpdateUseId(currentUser.getId());
            commonAttribute.setUpdateUseName(currentUser.getName());
            commonAttribute.setUpdateTime(LocalDateTime.now());
        }

    }

}
