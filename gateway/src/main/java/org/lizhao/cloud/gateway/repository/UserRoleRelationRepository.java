package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.relation.UserRoleRelation;
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

    /**
     * 根据 用户id 和 角色id 查询绑定关系
     * @param userId 用户id
     * @param roleId 角色id
     */
    Mono<UserRoleRelation> findByUserIdAndRoleId(String userId, String roleId);

}
