package org.lizhao.user.handler;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.model.TreeNode;
import org.lizhao.base.utils.BaseUtils;
import org.lizhao.user.model.GroupModel;
import org.lizhao.user.model.UserGroupModel;
import org.lizhao.user.repository.GroupUserRelationRepository;
import org.lizhao.user.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

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

    @Resource
    private GroupUserRelationRepository groupUserRelationRepository;

    public Flux<UserGroupModel> findUsersInGroup(String groupId) {
        return userRepository.findUsersByGroupId(groupId);
    }

    public Flux<User> findUsersNotInGroup(String groupId) {
        return userRepository.findUsersWithoutGroupId(groupId);
    }

    /**
     * 根据数据生成树
     * @param groups 组
     * @return 树
     */
    public List<GroupModel> generateTree(List<Group> originGroups) {
        if (ObjectUtils.isEmpty(originGroups)) {
            return Collections.emptyList();
        }

        List<GroupModel> groups = originGroups.stream()
                .map(e -> BaseUtils.copy(e, GroupModel.class))
                .toList();

        List<? extends TreeNode<GroupModel>> multiTree = BaseUtils.buildTree(groups, (current, parent) -> {
            String parentId = ((Group)current).getParentId();
            String id = ((Group)parent).getId();
            return Objects.equals(parentId, id);
        }, (current, children) -> {
            String parentId = ((Group)children).getParentId();
            String id = ((Group)current).getId();
            return Objects.equals(parentId, id);
        });

//        if (multiTree.size() > 1) {
//            throw new CustomException("数据中存在离散的节点");
//        }

        return (List<GroupModel>) multiTree;
    }

    /**
     * 将用户绑定到组
     * @return 用户已绑定的组
     */
    public Flux<GroupUserRelation> bindToGroups(Map<User, Collection<Group>> userGroupMap) {
        Publisher<GroupUserRelation> bound = CommonHandler.bind(userGroupMap,
                (user, group) -> {
                    if (user.getId() == null || group.getId() == null) {
                        return null;
                    }
                    GroupUserRelation relation = new GroupUserRelation();
                    relation.setGroupId(group.getId());
                    relation.setUserId(user.getId());
                    return relation;
                }, p -> groupUserRelationRepository.saveAll(Flux.from(p)));
        return Flux.from(bound);
    }

    /**
     * 将用户绑定到组
     * @return 用户已绑定的组
     */
    public Flux<GroupUserRelation> bindMoreToGroup(Map<Group, Collection<User>> userGroupMap) {
        Publisher<GroupUserRelation> bound = CommonHandler.bind(userGroupMap,
                (group, user) -> {
                    if (user.getId() == null || group.getId() == null) {
                        return null;
                    }
                    GroupUserRelation relation = new GroupUserRelation();
                    relation.setGroupId(group.getId());
                    relation.setUserId(user.getId());
                    return relation;
                }, groupUserRelationRepository::saveAll);
        return Flux.from(bound);
    }

    /**
     * 将用户与组解绑
     * @param relation 用户与组绑定关系
     */
    public Mono<Void> unbindFromGroup(GroupUserRelation relation) {

        if (relation == null) {
            return Mono.error(new Throwable("空的绑定关系"));
        }
        if (relation.getUserId() == null) {
            return Mono.error(new Throwable("用户id为空"));
        }
        if (relation.getGroupId() == null) {
            return Mono.error(new Throwable("用户组id为空"));
        }

        return groupUserRelationRepository.findByGroupIdAndUserId(relation.getGroupId(), relation.getUserId())
                .switchIfEmpty(Mono.error(new Throwable("不存在用户与组的绑定关系")))
                .collectList()
                .flatMap(l -> {
                    if (l.size() > 1) {
                        return Mono.error(new Throwable("存在重复的绑定的关系"));
                    }
                    return groupUserRelationRepository.deleteById(l.get(0).getId());
                });
    }
}
