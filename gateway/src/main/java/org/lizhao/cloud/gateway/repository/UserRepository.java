package org.lizhao.cloud.gateway.repository;

import org.lizhao.cloud.gateway.entity.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, String> {

     @Query("update \"user\" set status = ?2 where id = ?1")
     Mono<User> updateStatusById(String userId, short status);

}
