package org.lizhao.base.model.resource;

import org.lizhao.base.entity.authority.Authority;
import org.lizhao.base.entity.resource.ServerResource;
import org.lizhao.base.enums.BaseOperationEnum;
import org.lizhao.base.model.TreeNode;
import org.lizhao.base.utils.BaseUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Description 资源 model
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-24 17:53
 * @since 0.0.1-SNAPSHOT
 */
public class ServerResourceModel extends ServerResource implements TreeNode<ServerResourceModel> {

    private Map<BaseOperationEnum, Authority> needAuthorities;

    private Collection<TreeNode<ServerResourceModel>> children;

    public static ServerResourceModel of(ServerResource resource, Map<BaseOperationEnum, Authority> needAuthorities) {
        ServerResourceModel resourceModel = BaseUtils.copy(resource, ServerResourceModel.class);
        resourceModel.setNeedAuthorities(needAuthorities);
        return resourceModel;
    }

    public Map<BaseOperationEnum, Authority> getNeedAuthorities() {
        return needAuthorities;
    }

    public void setNeedAuthorities(Map<BaseOperationEnum, Authority> needAuthorities) {
        this.needAuthorities = needAuthorities;
    }

    @Override
    public Collection<TreeNode<ServerResourceModel>> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(Collection<TreeNode<ServerResourceModel>> children) {
        this.children = children;
    }
}
