package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.user.User;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

/**
 * Description User repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2023-07-12 22:55
 * @since jdk-1.8.0
 */
public interface UserRepository extends ReactiveCrudRepository<User, String>, ReactiveSortingRepository<User, String>, ReactiveQueryByExampleExecutor<User> {
}
