package org.lizhao.cloud.gateway.handler;

import com.sun.source.tree.Tree;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.entity.user.Group;
import org.lizhao.base.entity.user.User;
import org.lizhao.base.exception.CustomException;
import org.lizhao.base.model.Node;
import org.lizhao.base.model.TreeNode;
import org.lizhao.base.utils.BaseUtils;
import org.lizhao.cloud.gateway.model.UserGroupModel;
import org.lizhao.cloud.gateway.repository.UserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
    public List<Group> generateTree(List<Group> groups) {
        if (ObjectUtils.isEmpty(groups)) {
            throw new CustomException("传入组为空");
        }

        List<? extends TreeNode<Group>> multiTree = BaseUtils.buildTree(groups, (current, parent) -> {
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

        return (List<Group>) multiTree;
    }
}
