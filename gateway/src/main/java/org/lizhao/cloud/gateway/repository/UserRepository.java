package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.model.UserGroupModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 15259
 */
public interface UserRepository extends R2dbcRepository<User, String> {

     /**
      * 更新用户状态
      * @param userId 用户id
      * @param status 用户状态
      * @return true-更新成功;false-更新失败
      */
     @Query("update \"user\" set status = :status where id = :userId")
     Mono<Boolean> updateStatusById(String userId, short status);

     /**
      * 根据权限id查询绑定的用户
      * @param authorityId 权限id
      * @param valid 绑定关系是否有效
      * @return 绑定该权限的用户
      */
     @Query(
             "select u.* from \"user\" u" +
             "      left join user_authority_relation uar on uar.user_id = u.id" +
             "      where uar.authority_id = :authorityId" +
             "      and (case when :valid is not null then uar.valid=:valid" +
             "           else 1=1" +
             "           end)"
     )
     Flux<User> findUsersByAuthorityId(String authorityId, Boolean valid);

     /**
      * 查询组中的所有绑定用户
      * @param groupId 组id
      * @param valid 是否起效
      * @return 在组中的所有用户(valid相同)
      */
     @Query(
             "select u.id user_id," +
                     "u.name user_name," +
                     "u.status user_status," +
                     "gur.id relation_id," +
                     "gur.valid relation_valid," +
                     "\"g\".id group_id," +
                     "\"g\".name group_name," +
                     "\"g\".status group_status," +
                     "\"g\".comment group_comment" +
             "      from \"user\" u" +
             "      left join group_user_relation gur on gur.user_id = u.id" +
             "      left join \"group\" \"g\" on \"g\".id = gur.group_id" +
             "      where gur.group_id = :groupId" +
             "      and (case when :valid is not null then gur.valid=:valid" +
             "           else 1=1" +
             "           end)"
     )
     Flux<UserGroupModel> findUsersByGroupId(String groupId, Boolean valid);

     /**
      * 查询未绑定至组中对的所有用户
      * @param groupId 组id
      * @return 不在组中的所有用户
      */
     @Query(
             "select u.* from \"user\" u" +
                     "   where u.id not in (" +
                     "        select gur.user_id from group_user_relation gur" +
                     "        where gur.group_id = :groupId" +
                     ")"
     )
     Flux<User> findUsersWithoutGroupId(String groupId);

}
