package org.lizhao.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.lizhao.database.jpa.CommonAttributeAuditEntityListener;

/**
 * Description 追加信息
 * 默认所有实体都要继承
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-06-18 16:17
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@MappedSuperclass
//@EntityListeners(value = CommonAttributeAuditEntityListener.class)
public abstract class CommonAttribute implements Serializable {

    /**
     * 记录创建时使用的账号的Id
     */
    @Column
    private String createUseId;

    /**
     * 记录创建时使用的账号的用户名称
     */
    @Column
    private String createUseName;

    /**
     * 记录创建时的时间
     */
    @Column
    private LocalDateTime createTime;

    /**
     * 记录更新时使用账号的Id
     */
    @Column
    private String updateUseId;

    /**
     * 记录更新时使用账号的用户名称
     */
    @Column
    private String updateUseName;

    /**
     * 记录更新时的时间
     */
    @Column
    private LocalDateTime updateTime;

}
