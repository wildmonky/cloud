package org.lizhao.cloud.gateway.serviceImpl;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.user.Group;
import org.lizhao.cloud.gateway.repository.GroupRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Description 用户组服务
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-08 0:00
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class GroupServiceImpl {

    @Resource
    private GroupRepository groupRepository;

    public Mono<Group> save(Group group) {
        return groupRepository.save(group);
    }

    public Mono<Void> remove(Group group) {
        return groupRepository.delete(group);
    }

}
