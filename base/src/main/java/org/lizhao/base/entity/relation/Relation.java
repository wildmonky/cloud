package org.lizhao.base.entity.relation;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.user.User;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.time.LocalDateTime;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-11 18:52
 * @since 0.0.1-SNAPSHOT
 */

@Getter
@Setter
@MappedSuperclass
public class Relation {

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

}
