package org.lizhao.cloud.gateway.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.cloud.gateway.entity.user.User;
import org.lizhao.database.jpa.CommonAttributeAuditEntityListener;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description 追加信息
 * 默认所有实体都要继承
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 16:17
 * @since 0.0.1-SNAPSHOT
 * @see User
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(value = CommonAttributeAuditEntityListener.class)
public abstract class CommonAttribute implements Serializable {

    /**
     * 记录创建时使用的账号的Id
     * {@link User#getId()}
     */
    @Column
    @InsertOnlyProperty
    private String createUseId;

    /**
     * 记录创建时使用的账号的用户名称 {@link User#getName()}
     */
    @Column
    @InsertOnlyProperty
    private String createUseName;

    /**
     * 记录创建时的时间
     */
    @Column
    @InsertOnlyProperty
    private LocalDateTime createTime;

    /**
     * 记录更新时使用账号的Id {@link User#getId()}
     */
    @Column
    private String updateUseId;

    /**
     * 记录更新时使用账号的用户名称 {@link User#getName()}
     */
    @Column
    private String updateUseName;

    /**
     * 记录更新时的时间
     */
    @Column
    private LocalDateTime updateTime;

}
