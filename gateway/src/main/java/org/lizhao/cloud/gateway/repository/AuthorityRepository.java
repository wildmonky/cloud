package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.model.GroupAuthorityModel;
import org.lizhao.cloud.gateway.model.RoleAuthorityModel;
import org.lizhao.cloud.gateway.model.UserAuthorityModel;
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
     * 根据权限id获取绑定的组
     * @param authorityId 权限id
     * @return 绑定该用户的组
     */
    @Query(
            "select \"g\".id group_id," +
                    "\"g\".name group_name," +
                    "\"g\".status group_status," +
                    "\"g\".comment group_comment," +
                    "gar.id relation_id," +
                    "a.id authority_id," +
                    "a.name authority_name," +
                    "a.status authority_status," +
                    "a.comment authority_comment " +
                    "   from \"group\" \"g\" " +
                    "   left join group_authority_relation gar on gar.group_id = \"g\".id" +
                    "   left join authority a on gar.authority_id = a.id" +
                    "   where gar.authority_id = :authorityId"
    )
    Flux<GroupAuthorityModel> findGroupsByAuthorityId(String authorityId);

    /**
     * 根据权限id查询绑定的用户
     * @param authorityId 权限id
     * @return 绑定该权限的用户
     */
    @Query(
            "select u.id user_id," +
                    "u.name user_name," +
                    "u.phone user_phone," +
                    "u.status user_status," +
                    "uar.id relation_id," +
                    "a.id authority_id," +
                    "a.name authority_name," +
                    "a.status authority_status," +
                    "a.comment authority_comment " +
                    "      from \"user\" u" +
                    "      left join user_authority_relation uar on uar.user_id = u.id" +
                    "      left join authority a on uar.authority_id = a.id" +
                    "      where uar.authority_id = :authorityId"
    )
    Flux<UserAuthorityModel> findUsersByAuthorityId(String authorityId);

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

    /**
     * 根据权限获取绑定的角色
     * @param authorityId 权限id
     * @return 绑定该权限的角色
     */
    @Query(
            "select r.id role_id," +
                    "r.name role_name," +
                    "r.status role_status," +
                    "r.comment role_comment," +
                    "rar.id relation_id," +
                    "a.id authority_id," +
                    "a.name authority_name," +
                    "a.status authority_status," +
                    "a.comment authority_comment " +
                    "from \"role\" r" +
                    "   left join role_authority_relation rar on rar.role_id = r.id" +
                    "   left join authority a on rar.authority_id = a.id" +
                    "   where rar.authority_id = :authorityId"
    )
    Flux<RoleAuthorityModel> findRolesByAuthorityId(String authorityId);

    @Query(
            "select * from \"role\" r " +
                    "where id not in (" +
                    "   select role_id from role_authority_relation rar" +
                    "   where rar.authority_id = :authorityId" +
                    ")"
    )
    Flux<Role> findRolesWithoutAuthorityId(String authorityId);

}
