package org.lizhao.base.entity.realtime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.lizhao.base.entity.CommonAttribute;
import org.lizhao.base.jpa.IdentifierGeneratorImpl;

/**
 * Description 房间
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-05-07 16:59
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "room")
public class Room extends CommonAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-snowflake")
    @GenericGenerator(name = "custom-snowflake", type= IdentifierGeneratorImpl.class)
    private String id;

    private String name;

    /**
     * 房主id
     */
    private String ownerId;

    private Integer capacity;

    private Integer used;

    private String secret;

}
