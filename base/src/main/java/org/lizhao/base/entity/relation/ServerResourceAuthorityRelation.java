package org.lizhao.base.entity.relation;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.resource.ServerResource;
import org.springframework.data.annotation.Id;

/**
 * Description 资源对应的权限
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 21:28
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Table(name = "server_resource_authority_relation")
public class ServerResourceAuthorityRelation extends Relation{

    @Id
    private String id;

    /**
     * {@link ServerResource#getId()}
     */
    private String resourceId;

    /**
     * {@link Authority#getId()}
     */
    private String authorityId;

}
