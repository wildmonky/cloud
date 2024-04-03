package org.lizhao.cloud.gateway.repository;

import org.lizhao.cloud.gateway.entity.authority.Role;
import org.lizhao.cloud.gateway.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Description 角色仓库
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 21:14
 * @since jdk-1.8.0
 */
public interface RoleRepository extends R2dbcRepository<Role, String> {

    @Query("WITH RECURSIVE SubRole AS (" +
            "    SELECT *" +
            "    FROM \"role\" " +
            "    WHERE parent_id IS NULL -- 假设顶级类别的parent_id为NULL\n" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"role\" c" +
            "    INNER JOIN SubRole sc ON sc.id = c.parent_id)" +
            "SELECT * FROM SubRole;")
    Flux<Role> allChild();

    @Query("WITH RECURSIVE SubRole AS (" +
            "    SELECT *" +
            "    FROM \"role\" " +
            "    WHERE id IN (:ids)" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"role\" c" +
            "    INNER JOIN SubRole sc ON sc.id = c.parent_id)" +
            "SELECT * FROM SubRole;")
    Flux<Role> child(@Param("ids") Collection<String> roleIdFlux);

    @Query("update \"role\" set status = ?2 where id = ?1")
    Mono<Role> updateStatusById(String roleId, short status);

}
