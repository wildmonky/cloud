package org.lizhao.user.repository;

import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.user.model.GroupRoleModel;
import org.lizhao.user.model.UserRoleModel;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * @param parentId 父级角色id
     * @return 父级角色是否存在子级角色
     */
    Mono<Boolean> existsAllByParentId(String parentId);

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
     * @return 组所绑定的角色（不包含子角色）
     */
    @Query(
            "SELECT * FROM \"role\" r" +
            "   LEFT JOIN \"group_role_relation\" grr ON grr.role_id = r.id" +
            "   WHERE grr.group_id IN (:groupId)"
    )
    Flux<Role> rolesInGroup(Publisher<String> groupId);

    /**
     * 查询组绑定的所有角色（包含子角色）
     * @param groupId 组id
     * @return 组所绑定的所有角色
     */
    @Query(
            "WITH RECURSIVE SubRole AS (" +
            "    SELECT * FROM \"role\" WHERE id IN (" +
            "       SELECT * FROM \"role\" r" +
            "       LEFT JOIN \"group_role_relation\" grr ON grr.role_id = r.id" +
            "       WHERE grr.group_id IN (:groupId) " +
            "    )" +
            "    UNION ALL" +
            "    SELECT c.* FROM \"role\" c" +
            "    INNER JOIN SubRole sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubRole;"
    )
    Flux<Role> rolesIncludeChildInGroup(Publisher<String> groupId);

    /**
     * 查询用户绑定的角色（不包含子角色）
     * @param userId 用户id
     * @return 用户绑定的角色
     */
    @Query(
            "SELECT * FROM \"role\" r" +
            "   LEFT JOIN user_role_relation urr ON urr.role_id = r.id" +
            "   WHERE urr.user_id = :userId"
    )
    Flux<Role> findRolesByUserId(String userId);

    /**
     * 根据角色获取绑定的用户
     * @param roleId 角色id
     * @return 绑定该角色的用户
     */
    @Query(
            "select u.id user_id," +
                    "u.name user_name," +
                    "u.phone user_phone," +
                    "u.status user_status," +
                    "urr.id relation_id," +
                    "r.id role_id," +
                    "r.name role_name," +
                    "r.status role_status," +
                    "r.comment role_comment " +
                    "   from \"user\" u" +
                    "   left join user_role_relation urr on urr.user_id = u.id" +
                    "   left join \"role\" r on urr.role_id = r.id" +
                    "   where urr.role_id = :roleId"
    )
    Flux<UserRoleModel> findUsersByRoleId(String roleId);

    /**
     * 根据角色获取未绑定的用户
     * @param roleId 角色id
     * @return 未绑定该角色的用户
     */
    @Query(
            "select u.* from \"user\" u" +
                    "   where id not in (" +
                    "       select user_id from user_role_relation urr" +
                    "       where urr.role_id = :roleId" +
                    "   )"
    )
    Flux<User> findUsersWithoutRoleId(String roleId);

    /**
     * 根据角色获取绑定的组
     * @param roleId 角色id
     * @return 绑定该角色的组
     */
    @Query(
            "select \"g\".id group_id," +
                    "\"g\".name group_name," +
                    "\"g\".status group_status," +
                    "\"g\".comment group_comment," +
                    "grr.id relation_id," +
                    "r.id role_id," +
                    "r.name role_name," +
                    "r.status role_status," +
                    "r.comment role_comment " +
                    "   from \"group\" \"g\"" +
                    "       left join group_role_relation grr on grr.group_id = \"g\".id" +
                    "       left join \"role\" r on grr.role_id = r.id" +
                    "       where grr.role_id = :roleId"
    )
    Flux<GroupRoleModel> findGroupsByRoleId(String roleId);

    /**
     * 根据角色获取未绑定的组
     * @param roleId 角色id
     * @return 未绑定该角色的组
     */
    @Query(
            "select g.* from \"group\" \"g\"" +
                    "   where id not in (" +
                    "       select group_id from group_role_relation grr" +
                    "       where grr.role_id = :roleId" +
                    ")"
    )
    Flux<Group> findGroupsWithoutRoleId(String roleId);

}
