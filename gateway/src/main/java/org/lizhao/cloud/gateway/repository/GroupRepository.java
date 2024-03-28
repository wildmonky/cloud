package org.lizhao.cloud.gateway.repository;

import org.lizhao.cloud.gateway.entity.user.Group;
import org.lizhao.cloud.gateway.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface GroupRepository extends R2dbcRepository<Group, String> {

    @Query("WITH RECURSIVE SubGroup AS (" +
            "    SELECT *" +
            "    FROM \"group\"" +
            "    WHERE parent_id IS NULL" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"group\" c" +
            "    INNER JOIN SubGroup sc ON sc.id = c.parent_id)" +
            "SELECT * FROM SubGroup;")
    Flux<Group> allChild();

    @Query("WITH RECURSIVE SubGroup AS (" +
            "    SELECT *" +
            "    FROM \"group\"" +
            "    WHERE id IN (:ids)" +
            "    UNION ALL" +
            "    SELECT c.*" +
            "    FROM \"group\" c" +
            "    INNER JOIN SubGroup sc ON sc.id = c.parent_id)" +
            "SELECT * FROM SubGroup;")
    Flux<Group> child(@Param("ids") Collection<String> groupId);

    @Query("update \"group\" set status = ?2 where id = ?1")
    Mono<User> updateStatusById(String groupId, short status);

}
