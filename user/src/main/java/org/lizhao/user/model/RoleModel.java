package org.lizhao.user.model;

import org.lizhao.base.entity.authority.Role;
import org.lizhao.base.model.TreeNode;

import java.util.Collection;

/**
 * Description 角色模型
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 18:05
 * @since 0.0.1-SNAPSHOT
 */
public class RoleModel extends Role implements TreeNode<RoleModel> {

    private Collection<TreeNode<RoleModel>> children;

    @Override
    public Collection<TreeNode<RoleModel>> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(Collection<TreeNode<RoleModel>> children) {
        this.children = children;
    }
}
