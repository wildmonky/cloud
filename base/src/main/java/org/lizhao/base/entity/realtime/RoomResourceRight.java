package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-06-30 18:32
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "room_resource_right")
public class RoomResourceRight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
    private String id;

    /**
     * {@link RoomResource#getId()}
     */
    private String roomResourceId;

    /**
     * {@link RoomMember#getId()}
     */
    private String roomMemberId;

}
