package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

/**
 * Description 房间成员客户端
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-08 16:54
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "room_resource")
public class RoomResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
    private String id;

    /**
     * {@link Room#getId()}
     */
    private String roomId;

    /**
     * {@link RoomMember#getId()}
     */
    private String roomMemberId;

    /**
     * realtime (播放/推流)客户端id，可用于踢出
     */
    private String clientId;

    /**
     * {@link org.lizhao.base.enums.realtime.PlatformEnum}
     * 客户端类型：srs 或其他
     */
    private String platform;

    /**
     * 资源的访问地址，如：
     * 1、一对一通话视频流地址；
     */
    private String url;

    /**
     * 资源的内容，如信息
     */
    private String content;

    /**
     * 资源类型：1、流;2、文字信息
     */
    private Integer type;

    /**
     * {@link org.lizhao.base.enums.ResourceStateEnum}
     */
    private Integer status ;

    /**
     * 是否设置了权限
     */
    private Boolean needRight;

}
