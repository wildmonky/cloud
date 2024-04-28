package org.lizhao.user.model;

import org.lizhao.base.entity.user.Group;
import org.lizhao.base.model.TreeNode;

import java.util.Collection;

/**
 * Description 用户组 模型
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 17:56
 * @since 0.0.1-SNAPSHOT
 */
public class GroupModel extends Group implements TreeNode<GroupModel> {

    private Collection<TreeNode<GroupModel>> children;

    @Override
    public Collection<TreeNode<GroupModel>> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Collection<TreeNode<GroupModel>> children) {
        this.children = children;
    }
}
