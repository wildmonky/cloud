package org.lizhao.base.model;;

import java.util.Collection;

/**
 * Description 树形数据
 * json TypeReference<T>
 *
 * @see com.fasterxml.jackson.core.type.TypeReference
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-12 23:29
 * @since 0.0.1-SNAPSHOT
 */
public interface TreeNode<T> {

    Collection<TreeNode<T>> getChildren();

    void setChildren(Collection<TreeNode<T>> children);

}
