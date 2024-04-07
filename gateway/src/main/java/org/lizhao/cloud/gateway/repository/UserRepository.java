package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, String> {

     /**
      * 更新用户状态
      * @param userId 用户id
      * @param status 用户状态
      * @return true-更新成功;false-更新失败
      */
     @Query("update \"user\" set status = :status where id = :userId")
     Mono<Boolean> updateStatusById(String userId, short status);

}
