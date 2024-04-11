package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.authority.Authority;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Description 权限仓库
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 21:13
 * @since jdk-1.8.0
 */
public interface AuthorityRepository extends R2dbcRepository<Authority, String> {

    /**
     * 更新权限的状态
     * @param authorityId 权限id
     * @param status 更新后状态
     * @return true-更新成功;false-更新失败
     */
    @Query("update \"authority\" set status = :status where id = :authorityId")
    Mono<Boolean> updateStatusById(String authorityId, short status);

    /**
     * 查询用户所绑定的权限
     * @param userId 用户id
     * @param valid 绑定关系：
     *              null-所有绑定的权限;
     *              true-起效的权限;
     *              false-未起效的权限
     * @return 用户绑定的权限
     */
    @Query(
            "SELECT * FROM \"authority\" a" +
            "   LEFT JOIN user_authority_relation uar ON uar.authority_id = a.id" +
            "   WHERE uar.user_id IN (:userId)" +
            "   AND (CASE WHEN :valid IS NOT NULL THEN uar.valid = :valid" +
            "           ELSE 1=1" +
            "        END" +
            "   )"
    )
    Flux<Authority> findAuthoritiesByUsers(Publisher<String> userId, Boolean valid);


    /**
     * 查询组所绑定的权限
     * @param groupId 组id
     * @param valid 绑定关系：
     *              null-所有绑定的权限;
     *              true-起效的权限;
     *              false-未起效的权限
     * @return 组绑定的权限
     */
    @Query("SELECT * FROM \"authority\" a" +
            "   LEFT JOIN group_authority_relation gar ON gar.authority_id = a.id" +
            "   WHERE gar.group_id IN (:groupId)" +
            "   AND (CASE WHEN :valid IS NOT NULL THEN gar.valid = :valid" +
            "           ELSE 1=1" +
            "        END" +
            "   )"
    )
    Flux<Authority> findAuthoritiesByGroups(Publisher<String> groupId, Boolean valid);

    /**
     * 查询角色所绑定的权限
     * @param roleId 角色id
     * @param valid 绑定关系：
     *              null-所有绑定的权限;
     *              true-起效的权限;
     *              false-未起效的权限
     * @return 角色绑定的权限
     */
    @Query("SELECT * FROM \"authority\" a" +
            "   LEFT JOIN role_authority_relation rar ON rar.authority_id = a.id" +
            "   WHERE rar.role_id IN (:roleId)" +
            "   AND (CASE WHEN :valid IS NOT NULL THEN rar.valid = :valid" +
            "               ELSE 1=1" +
            "        END" +
            "   )"
    )
    Flux<Authority> findAuthoritiesByRoles(Publisher<String> roleId, Boolean valid);

}
