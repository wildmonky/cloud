package org.lizhao.base.entity;

import lombok.Getter;
import lombok.Setter;

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
public class App extends AppendInfo{

    /**
     * 应用Id
     */
    private String id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用别名
     */
    private String alisaName;

    /**
     * 应用路径
     */
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
