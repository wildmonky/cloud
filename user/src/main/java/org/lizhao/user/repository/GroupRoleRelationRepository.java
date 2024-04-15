package org.lizhao.user.repository;

import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-06 14:35
 * @since jdk-1.8.0
 */
public interface GroupRoleRelationRepository extends R2dbcRepository<GroupRoleRelation, String> {

    /**
     * 根据 组id 和 角色id 查询关系
     * @param groupId 组id
     * @param roleId 角色id
     */
    Mono<GroupRoleRelation> findByGroupIdAndRoleId(String groupId, String roleId);

}
