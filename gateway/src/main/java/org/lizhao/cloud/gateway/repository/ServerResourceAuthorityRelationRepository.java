package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.relation.ServerResourceAuthorityRelation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * Description 资源权限关系 repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-24 22:13
 * @since jdk-1.8.0
 */
public interface ServerResourceAuthorityRelationRepository extends R2dbcRepository<ServerResourceAuthorityRelation, String> {

    /**
     * 获取资源需要的权限
     * @param resourceId 资源Id
     * @return 资源对应的权限
     */
    Flux<ServerResourceAuthorityRelation> findServerResourceAuthorityRelationsByResourceId(String resourceId);

    /**
     * 查询 资源 绑定的权限。
     * 操作该资源时所需的权限
     *
     * @param resourceId 资源Id
     * @return 资源权限
     */
    @Query(
            "select a.* from server_resource_authority_relation srar " +
                    "left join \"authority\" a on srar.authority_id = a.id " +
                    "where srar.resource_id = :resourceId"
    )
    Flux<Authority> findServerResourceAuthoritiesByResourceId(String resourceId);

}
