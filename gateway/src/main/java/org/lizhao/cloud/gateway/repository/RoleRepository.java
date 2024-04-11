package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

/**
 * Description 角色仓库
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 21:14
 * @since jdk-1.8.0
 */
public interface RoleRepository extends R2dbcRepository<Role, String> {

    /**
     * 从根角色获取所有关联的角色节点
     * @return 所有关联的角色节点
     */
    @Query(
            "WITH RECURSIVE SubRole AS (" +
            "    SELECT *" +
            "    FROM \"role\" " +
            "    WHERE parent_id IS NULL -- 假设顶级类别的parent_id为NULL\n" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"role\" c" +
            "    INNER JOIN SubRole sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubRole;")
    Flux<Role> allChild();

    /**
     * 根据角色id获取关联的子节点
     * @param roleId 角色id
     * @return roleIds 关联的所有子角色
     */
    @Query(
            "WITH RECURSIVE SubRole AS (" +
            "    SELECT * FROM \"role\" " +
            "    WHERE id IN (:id)" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"role\" c" +
            "    INNER JOIN SubRole sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubRole;"
    )
    Flux<Role> child(@Param("id") Publisher<String> roleId);

    /**
     * 更新角色的状态
     * @param roleId 角色id
     * @param status 更新后状态
     * @return true-更新成功；false-更新失败（返回的是更新影响记录条数，1-true;0-false）
     */
    @Query("update \"role\" set status = :status where id = :roleId")
    Mono<Boolean> updateStatusById(String roleId, short status);

    /**
     * 获取组所绑定的角色
     * @param groupId 组id
     * @param valid 绑定关系:
     *              null-查询所有;
     *              true-查询起效;
     *              false-查询未起效
     * @return 组所绑定的角色（不包含子角色）
     */
    @Query(
            "SELECT * FROM \"role\" r" +
            "   LEFT JOIN \"group_role_relation\" grr ON grr.role_id = r.id" +
            "   WHERE grr.group_id IN (:groupId)" +
            "   AND (CASE WHEN :valid IS NOT NULL THEN grr.valid = :valid" +
            "           ELSE 1=1" +
            "        END" +
            "   )"
    )
    Flux<Role> rolesInGroup(Publisher<String> groupId, Boolean valid);

    /**
     * 查询组绑定的所有角色（包含子角色）
     * @param groupId 组id
     * @param valid 绑定关系:
     *              null-查询所有子角色;
     *              true-查询起效的子角色;
     *              false-查询未起效的子角色;
     * @return 组所绑定的所有角色
     */
    @Query(
            "WITH RECURSIVE SubRole AS (" +
            "    SELECT * FROM \"role\" WHERE id IN (" +
            "       SELECT * FROM \"role\" r" +
            "       LEFT JOIN \"group_role_relation\" grr ON grr.role_id = r.id" +
            "       WHERE grr.group_id IN (:groupId) " +
            "       AND (CASE WHEN :valid IS NOT NULL THEN grr.valid = :valid" +
            "                 ELSE 1=1" +
            "               END" +
            "       )" +
            "    )" +
            "    UNION ALL" +
            "    SELECT c.* FROM \"role\" c" +
            "    INNER JOIN SubRole sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubRole;"
    )
    Flux<Role> rolesIncludeChildInGroup(Publisher<String> groupId, Boolean valid);

    /**
     * 查询用户绑定的角色（不包含子角色）
     * @param userId 用户id
     * @param valid 绑定关系：
     *              null-所有绑定的角色;
     *              true-起效的角色;
     *              false-未起效的角色;
     * @return 用户绑定的角色
     */
    @Query(
            "SELECT * FROM \"role\" r" +
            "   LEFT JOIN user_role_relation urr ON urr.role_id = r.id" +
            "   WHERE urr.user_id = :userId" +
            "   AND (CASE WHEN :valid IS NOT NULL THEN urr.valid = :valid" +
            "           ELSE 1=1" +
            "           END" +
            "   )"
    )
    Flux<Role> findRolesByUserId(String userId, Boolean valid);

    /**
     * 根据权限获取绑定的角色
     * @param authorityId 权限id
     * @param valid 是否起效
     * @return 绑定该权限的角色
     */
    @Query(
            "select r.* from \"role\" r" +
            "   left join role_authority_relation rar on rar.role_id = r.id" +
            "   where rar.authority_id = :authorityId" +
            "   and (case when :valid is not null then rar.valid = :valid" +
            "       else 1=1" +
            "       end)"
    )
    Flux<Role> findRolesByAuthorityId(String authorityId, Boolean valid);

    /**
     * 根据角色获取绑定的用户
     * @param roleId 角色id
     * @param valid 是否起效
     * @return 绑定该角色的用户
     */
    @Query(
            "select u.* from \"user\" u" +
                    "   left join user_role_relation urr on urr.user_id = u.id" +
                    "   where urr.role_id = :roleId" +
                    "   and (case when :valid is not null then urr.valid = :valid" +
                    "       else 1=1" +
                    "       end)"
    )
    Flux<User> findUsersByRoleId(String roleId, Boolean valid);

    /**
     * 根据角色获取绑定的组
     * @param roleId 角色id
     * @param valid 是否起效
     * @return 绑定该角色的组
     */
    @Query(
            "select g.* from \"group\" \"g\"" +
                    "   left join group_role_relation grr on grr.group_id = u.id" +
                    "   where grr.role_id = :roleId" +
                    "   and (case when :valid is not null then grr.valid = :valid" +
                    "       else 1=1" +
                    "       end)"
    )
    Flux<Group> findGroupsByRoleId(String roleId, Boolean valid);

}
