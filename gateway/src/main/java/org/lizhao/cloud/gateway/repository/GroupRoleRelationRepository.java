package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-04-06 14:35
 * @since jdk-1.8.0
 */
public interface GroupRoleRelationRepository extends R2dbcRepository<GroupRoleRelation, String> {
}
