package org.lizhao.cloud.gateway.repository;

import org.lizhao.cloud.gateway.entity.authority.Authority;
import org.lizhao.cloud.gateway.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Description 权限仓库
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2024-03-25 21:13
 * @since jdk-1.8.0
 */
public interface AuthorityRepository extends R2dbcRepository<Authority, String> {

    @Query("WITH RECURSIVE SubAuthority AS (" +
            "    SELECT *" +
            "    FROM \"group\"" +
            "    WHERE parent_id IS NULL -- 假设顶级类别的parent_id为NULL\n" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"group\" c" +
            "    INNER JOIN SubAuthority sc ON sc.id = c.parent_id)" +
            "SELECT * FROM SubAuthority;")
    Flux<Authority> allChild();

    @Query("WITH RECURSIVE SubAuthority AS (" +
            "    SELECT *" +
            "    FROM \"group\"" +
            "    WHERE id IN (:ids)" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"group\" c" +
            "    INNER JOIN SubAuthority sc ON sc.id = c.parent_id)" +
            "SELECT * FROM SubAuthority;")
    Flux<Authority> child(@Param("ids") Collection<String> ids);

    @Query("update \"authority\" set status = ?2 where id = ?1")
    Mono<User> updateStatusById(String userId, short status);

}
