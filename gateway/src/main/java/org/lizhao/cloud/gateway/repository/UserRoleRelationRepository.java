package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * Description 用户-角色关系权限
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 22:18
 * @since jdk-1.8.0
 */
public interface UserRoleRelationRepository extends R2dbcRepository<UserRoleRelation, String> {

    @Query("update user_role_relation set status = ?2 where id = ?1")
    Mono<User> updateStatusById(String userId, boolean valid);

}
