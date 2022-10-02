package org.lizhao.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description 应用实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2022-09-25 16:21
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table
public class App extends CommonAttribute {

    /**
     * 应用Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "org.lizhao.database.jpa.IdentifierGeneratorImpl")
    private String id;

    /**
     * 应用名称
     */
    @Column
    private String name;

    /**
     * 应用别名
     */
    @Column
    private String alisaName;

    /**
     * 应用路径
     */
    @Column
    private String urlPath;

    /**
     * 应用部署ip
     */
    private String ip;

    /**
     * 应用部署端口
     */
    private Integer port;

    /**
     * 应用状态：0-初始；1-可用
     */
    private Integer status;
}
