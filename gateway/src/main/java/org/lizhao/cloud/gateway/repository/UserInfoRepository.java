package org.lizhao.cloud.gateway.repository;

import org.lizhao.base.entity.user.UserInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * Description User repository
 *
 * @author lizhao
 * @version 1.0.0
 * @date 2023-07-12 22:55
 * @since jdk-1.8.0
 */
public interface UserInfoRepository extends R2dbcRepository<UserInfo, String> {
}
