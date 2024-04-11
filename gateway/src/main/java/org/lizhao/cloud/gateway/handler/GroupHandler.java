package org.lizhao.cloud.gateway.handler;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.model.UserGroupModel;
import org.lizhao.cloud.gateway.repository.UserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Description 用户组 handler
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-10 12:21
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class GroupHandler {

    @Resource
    private UserRepository userRepository;

    public Flux<UserGroupModel> findUsersInGroup(String groupId, Boolean valid) {
        return userRepository.findUsersByGroupId(groupId, valid);
    }

    public Flux<User> findUsersNotInGroup(String groupId) {
        return userRepository.findUsersWithoutGroupId(groupId);
    }

}
