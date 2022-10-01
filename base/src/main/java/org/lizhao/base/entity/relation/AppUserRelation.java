package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.App;
import org.lizhao.base.entity.AppendInfo;
import org.lizhao.base.entity.user.User;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description 应用实体{@link App}和 用户实体{@link User}的关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:29
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "app_user_relation")
public class AppUserRelation extends AppendInfo {

    /**
     * {@link App} 和 {@link User} 关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 应用Id {@link App#getId()}
     */
    @Column
    private String appId;

    /**
     * 用户Id {@link User#getId()}
     */
    @Column
    private String userId;

    /**
     * 关系是否有效：true-有效；false|null-无效
     */
    @Column
    private Boolean valid;
}
