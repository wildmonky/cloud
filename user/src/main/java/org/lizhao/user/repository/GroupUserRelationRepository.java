package org.lizhao.user.repository;

import org.lizhao.base.entity.relation.GroupUserRelation;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Description 组-用户 关系仓库
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 22:16
 * @since jdk-1.8.0
 */
public interface GroupUserRelationRepository extends R2dbcRepository<GroupUserRelation, String> {

    @Query("update group_user_relation set status = ?2 where id = ?1")
    Mono<GroupUserRelation> updateStatusById(String userId, boolean valid);

    @Query("select * from group_user_relation where group_id = :groupId and user_id = :userId")
    Flux<GroupUserRelation> findByGroupIdAndUserId(String groupId, String userId);

    Mono<Boolean> existsAllByGroupId(String groupId);

}
