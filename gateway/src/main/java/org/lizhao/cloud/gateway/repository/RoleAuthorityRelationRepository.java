package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.relation.RoleAuthorityRelation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Description 角色-权限关系仓库
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 22:16
 * @since jdk-1.8.0
 */
public interface RoleAuthorityRelationRepository extends R2dbcRepository<RoleAuthorityRelation, String> {

    @Query("update role_authority_relation set status = ?2 where id = ?1")
    Mono<Boolean> updateStatusById(String userId, boolean valid);

    Flux<RoleAuthorityRelation> findByRoleIdAndAuthorityId(String roleId, String authorityId);

}
