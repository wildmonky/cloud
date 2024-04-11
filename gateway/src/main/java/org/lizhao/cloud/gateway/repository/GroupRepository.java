package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.user.Group;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GroupRepository extends R2dbcRepository<Group, String> {

    /**
     * 查询所有关联的节点
     * @return 组
     */
    @Query(
            "WITH RECURSIVE SubGroup AS (" +
            "    SELECT * FROM \"group\"" +
            "    WHERE parent_id IS NULL" +
            "    UNION ALL" +
            "    SELECT c.* FROM \"group\" c" +
            "    INNER JOIN SubGroup sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubGroup;"
    )
    Flux<Group> allChild();

    /**
     * 查询组下的子节点
     * @param groupId 组id
     * @return 子组
     */
    @Query(
            "WITH RECURSIVE SubGroup AS (" +
            "    SELECT * FROM \"group\"" +
            "    WHERE id IN (:ids)" +
            "    UNION ALL" +
            "    SELECT c.* FROM \"group\" c" +
            "    INNER JOIN SubGroup sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubGroup;"
    )
    Flux<Group> child(@Param("ids") Publisher<String> groupId);

    /**
     * 更新组状态
     * @param groupId 组id
     * @param status 状态
     * @return 更改结果：true-成功；false-失败
     */
    @Query("update \"group\" set status = :status where id = :groupId")
    Mono<Boolean> updateStatusById(String groupId, short status);

    /**
     * 查询 用户绑定的用户组
     * @param userId 用户Id
     * @return 用户直接绑定的用户组
     */
    @Query(
            "select * from \"group\" \"g\" " +
            "left join group_user_relation gur on gur.group_id = \"g\".id " +
            "where gur.user_id = :userId "
    )
    Flux<Group> findGroupsByUserId(String userId);


    /**
     * 查询 用户绑定的用户组（包含子组）
     * @param userId 用户id
     * @return 用户绑定的用户组（包含子组）
     */
    @Query(
            "WITH RECURSIVE SubGroup AS (" +
            "    SELECT * FROM \"group\" WHERE id IN (" +
            "       select \"g\".id from \"group\" \"g\"" +
            "       left join group_user_relation gur on gur.group_id = \"g\".id" +
            "       where gur.user_id = :userId " +
            "     )" +
            "    UNION ALL" +
            "    SELECT c.* FROM \"group\" c" +
            "    INNER JOIN SubGroup sc ON sc.id = c.parent_id" +
            ")" +
            "SELECT * FROM SubGroup;"
    )
    Flux<Group> findGroupsIncludeChildByUserId(String userId);

    /**
     * 根据权限id获取绑定的组
     * @param authorityId 权限id
     * @return 绑定该用户的组
     */
    @Query(
            "select \"g\".* from \"group\" \"g\" " +
            "   left join group_authority_relation gar on gar.group_id = \"g\".id" +
            "   where gar.authority_id = :authorityId"
    )
    Flux<Group> findGroupsByAuthorityId(String authorityId);

}
