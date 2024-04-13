package org.lizhao.base.model;

import java.util.Objects;
import java.util.Set;

/**
 * Description TODO
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-13 21:17
 * @since 0.0.1-SNAPSHOT
 */
public class Node<T> {

    T current;

    Set<Node<T>> children;

    public T get() {
        return current;
    }

    public void set(T current) {
        this.current = current;
    }

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    public Set<Node<T>> getChildren() {
        return children;
    }

    public void setChildren(Set<Node<T>> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node<?> treeNode = (Node<?>) o;

        if (!Objects.equals(current, treeNode.current)) {
            return false;
        }
        return Objects.equals(children, treeNode.children);
    }

    @Override
    public int hashCode() {
        int result = current != null ? current.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

}
