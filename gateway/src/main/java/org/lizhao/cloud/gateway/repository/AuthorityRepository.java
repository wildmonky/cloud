package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
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
     * @return 用户绑定的权限
     */
    @Query(
            "SELECT * FROM \"authority\" a" +
            "   LEFT JOIN user_authority_relation uar ON uar.authority_id = a.id" +
            "   WHERE uar.user_id IN (:userId)"
    )
    Flux<Authority> findAuthoritiesByUsers(Publisher<String> userId);


    /**
     * 查询组所绑定的权限
     * @param groupId 组id
     * @return 组绑定的权限
     */
    @Query("SELECT * FROM \"authority\" a" +
            "   LEFT JOIN group_authority_relation gar ON gar.authority_id = a.id" +
            "   WHERE gar.group_id IN (:groupId)"
    )
    Flux<Authority> findAuthoritiesByGroups(Publisher<String> groupId);

    /**
     * 未绑定该权限的组
     * @param authorityId 权限id
     * @return 未绑定该权限的组
     */
    @Query(
            "select \"g\".* from \"group\" \"g\"" +
                    "   where \"g\".id not in (" +
                    "       select gar.group_id from group_authority_relation gar" +
                    "       where gar.authority_id = :authorityId" +
                    ")"
    )
    Flux<Group> findGroupsWithoutAuthorityId(String authorityId);

    /**
     * 查询角色所绑定的权限
     * @param roleId 角色id
     * @return 角色绑定的权限
     */
    @Query("SELECT * FROM \"authority\" a" +
            "   LEFT JOIN role_authority_relation rar ON rar.authority_id = a.id" +
            "   WHERE rar.role_id IN (:roleId)"
    )
    Flux<Authority> findAuthoritiesByRoles(Publisher<String> roleId);

    /**
     * 根据权限id查询未绑定的用户
     * @param authorityId 权限id
     * @return 未绑定该权限的用户
     */
    @Query(
            "select u.* from \"user\" u" +
                    "   where u.id not in (" +
                    "      select uar.user_id from user_authority_relation uar " +
                    "      where uar.authority_id = :authorityId" +
                    ")"
    )
    Flux<User> findUsersWithoutAuthorityId(String authorityId);

    @Query(
            "select * from \"role\" r " +
                    "where id not in (" +
                    "   select role_id from role_authority_relation rar" +
                    "   where rar.authority_id = :authorityId" +
                    ")"
    )
    Flux<Role> findRolesWithoutAuthorityId(String authorityId);

}
