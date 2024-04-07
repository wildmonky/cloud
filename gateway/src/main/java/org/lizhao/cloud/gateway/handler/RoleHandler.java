package org.lizhao.cloud.gateway.handler;

import jakarta.annotation.Resource;
import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.entity.relation.GroupRoleRelation;
import org.lizhao.base.entity.relation.UserRoleRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.cloud.gateway.repository.GroupRoleRelationRepository;
import org.lizhao.cloud.gateway.repository.UserRoleRelationRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;

/**
 * Description 角色 handler
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 21:20
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class RoleHandler {

    @Resource
    private UserRoleRelationRepository userRoleRelationRepository;
    @Resource
    private GroupRoleRelationRepository groupRoleRelationRepository;

    /**
     * 绑定角色与用户之间的关系
     * @param userRoleMap 用户-角色 map
     * @return 绑定的用户-角色关系
     */
    public Flux<UserRoleRelation> bindToUser(Map<User, Collection<Role>> userRoleMap) {
        Publisher<UserRoleRelation> bound = CommonHandler.bind(userRoleMap,
                (user, role) -> {
                    if (user.getId() == null || role.getId() == null) {
                        return null;
                    }
                    UserRoleRelation relation = new UserRoleRelation();
                    relation.setUserId(user.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }, p -> userRoleRelationRepository.saveAll(Flux.from(p))
        );
        return Flux.from(bound);
    }

    /**
     * 绑定组与用户之间的关系
     * @param groupRoleMap 组-角色 map
     * @return 绑定的组-角色关系
     */
    public Flux<GroupRoleRelation> bindToGroup(Map<Group, Collection<Role>> groupRoleMap) {
        Publisher<GroupRoleRelation> bound = CommonHandler.bind(groupRoleMap,
                (group, role) -> {
                    if (group.getId() == null || role.getId() == null) {
                        return null;
                    }
                    GroupRoleRelation relation = new GroupRoleRelation();
                    relation.setGroupId(group.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }, p -> groupRoleRelationRepository.saveAll(Flux.from(p)));

        return Flux.from(bound);
    }

}
