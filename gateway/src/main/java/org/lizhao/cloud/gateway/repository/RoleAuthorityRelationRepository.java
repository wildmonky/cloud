package org.lizhao.cloud.gateway.repository;

import org.lizhao.cloud.gateway.entity.relation.RoleAuthorityRelation;
import org.lizhao.cloud.gateway.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 22:16
 * @since jdk-1.8.0
 */
public interface RoleAuthorityRelationRepository extends R2dbcRepository<RoleAuthorityRelation, String> {

    @Query("update role_authority_relation set status = ?2 where id = ?1")
    Mono<User> updateStatusById(String userId, boolean valid);

}
