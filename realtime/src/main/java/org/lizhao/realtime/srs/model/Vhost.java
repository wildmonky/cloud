package org.lizhao.realtime.srs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description srs vHost Model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 15:07
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class Vhost {

    String id;

    String name;

    /*
    vHost 中的 client客户端数量
     */
    Integer clients;

    /*
    vHost中的 stream流数量
     */
    Integer streams;

    /*
    vHost是否可用
     */
    Boolean enabled;

}
