package org.lizhao.base.entity.relation;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.App;
import org.lizhao.base.entity.AppendInfo;
import org.lizhao.base.entity.authority.Authority;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description 应用实体{@link App} 和  权限实体{@link Authority} 的 关系实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:59
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "app_authority_relation")
public class AppAuthorityRelation extends AppendInfo {

    /**
     * 应用实体{@link App} 和 权限实体{@link Authority} 的关系Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 应用id {@link App#getId()}
     */
    @Column
    private String appId;

    /**
     * 权限Id {@link Authority#getId()}
     */
    @Column
    private String authorityId;

    /**
     * 关系是否有有效：true-起效；false|null-失效
     */
    @Column
    private Boolean valid;
}
