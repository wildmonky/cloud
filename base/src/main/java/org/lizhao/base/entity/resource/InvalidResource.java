package org.lizhao.base.entity.resource;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Description 失效资源实体
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 16:44
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name= "invalid_resource")
public class InvalidResource {

    @Id
    private String id;

    private String originSourceId;

    private String optUserId;

    private LocalDateTime invalidTime;

}
