package org.lizhao.base.utils;

import org.lizhao.base.exception.CustomException;
import org.lizhao.base.model.Node;
import org.lizhao.base.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseUtils {
    private static final Logger log = LoggerFactory.getLogger(BaseUtils.class);

    private BaseUtils() {
    }

    @SafeVarargs
    public static <T> List<T> newArrayList(T... ts) {
        return ts != null && ts.length != 0 ? new ArrayList<>(Arrays.asList(ts)) : new ArrayList<>();
    }

    public static <S, T> T copy(S source, Class<T> clazz) {
        return copy(source, clazz, null);
    }

    public static <S, T> T copy(S source, Class<T> clazz, BiConsumer<S, T> consumer) {
        if (source == null) {
            return null;
        } else {
            try {
                T t = clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, t);
                if (consumer != null) {
                    consumer.accept(source, t);
                }
                return t;
            } catch (IllegalAccessException | InstantiationException var4) {
                log.error("BaseUtil.copy error", var4);
                return null;
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <S, T> List<T> copyList(List<S> source, Class<T> clazz) {
        return copyList(source, clazz, null);
    }

    public static <S, T> List<T> copyList(List<S> source, Class<T> clazz, BiConsumer<S, T> consumer) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        } else {
            return consumer == null ?
                    (source.size() > 200 ? source.parallelStream() : source.stream()).map((s) -> copy(s, clazz)).collect(Collectors.toList())
                    : (source.size() > 20 ? source.parallelStream() : source.stream()).map((s) -> copy(s, clazz, consumer)).collect(Collectors.toList());
        }
    }

    public static <S, T> List<T> copyList(List<S> source, Class<T> clazz, BiConsumer<S, T> consumer, boolean parallel) {
        return CollectionUtils.isEmpty(source) ?
                Collections.emptyList()
                : (parallel ? source.parallelStream() : source.stream()).map((s) -> copy(s, clazz, consumer)).collect(Collectors.toList());
    }

    public static <K, V> Map<K, V> copy(Map<K, V> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyMap();
        }

        return new HashMap<>(source);
    }

    public static <K, V, T> Map<K, T> mapping(Map<K, V> map, Function<V, T> func) {
        if (CollectionUtils.isEmpty(map)) {
            return Collections.emptyMap();
        }

        Map<K, T> mp = new HashMap<>();
        for (Entry<K, V> en : map.entrySet()) {
            mp.put(en.getKey(), func.apply(en.getValue()));
        }
        return mp;
    }

    public static <T> List<T> collect(Collection<List<T>> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        for (List<T> cs : collection) {
            if (!CollectionUtils.isEmpty(cs)) {
                list.addAll(cs);
            }
        }
        return list;
    }

    public static boolean isNumber(String text) {
        return text != null && text.matches("^[0-9]+$");
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime time) {
        return time == null ? null : Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static long toTimestamp(LocalDateTime time) {
        return time == null ? -1L : time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    /**
     * 根据数据生成树
     *
     * @param collection 数据
     * @param findParentFunc 寻找父亲节点的方法
     * @param findChildrenFunc 寻找子节点的方法
     * @return 生成的树
     * @param <T> 泛型
     */
    public static <T> List<Node<T>> generateTree(Collection<T> collection,
                                                     BiFunction<T, T, Boolean> findParentFunc,
                                                     BiFunction<T, T, Boolean> findChildrenFunc
    ) {
        if (ObjectUtils.isEmpty(collection)) {
            throw new CustomException("传入数据为空");
        }
        // parentId-child parentId 能为空
        List<Node<T>> multiTree = new ArrayList<>();

        Iterator<T> ite = collection.iterator();
        // 注意： 不要改成 for循环，下面操作涉及集合节点删除
        while(ite.hasNext()) {
            T node = ite.next();
            collection.remove(node);

            Node<T> current = new Node<>();

            Node<T> parent = treeParent(current, collection, findParentFunc);

            if (parent.equals(current)) {
                treeChildren(parent, collection, findChildrenFunc);
            } else {
                for (Node<T> childrenNode : parent.getChildren()) {
                    treeChildren(childrenNode, collection, findChildrenFunc);
                }
            }
            ite = collection.stream().iterator();
            multiTree.add(parent);
        }
        return multiTree;
    }

    /**
     * 寻找当前节点的父亲节点
     *
     * @param current 当前节点
     * @param collection 数据
     * @param equalsFunc 父节点比较方法
     * @return 父亲节点
     * @param <T> 泛型
     */
    private static <T> Node<T> treeParent(Node<T> current, Collection<T> collection,
                                          BiFunction<T, T, Boolean> equalsFunc) {

        T parent = collection.stream()
                .filter(p -> equalsFunc.apply(current.get(), p)).findFirst()
                .orElse(null);

        if (parent == null) {
            return current;
        }

        collection.remove(parent);

        Node<T> parentNode = new Node<T>();
        parentNode.set(parent);


        Set<Node<T>> child = new HashSet<>();
        child.add(current);
        parentNode.setChildren(child);
        treeParent(parentNode, collection, equalsFunc);

        return parentNode;
    }

    /**
     * 从数据中寻找子节点并装入当前节点
     *
     * @param current 当前节点
     * @param collection 数据
     * @param equalsFunc 子节点比较方法
     * @param <T> 泛型
     */
    private static <T> void treeChildren(Node<T> current, Collection<T> collection,
                                         BiFunction<T, T, Boolean> equalsFunc
    ) {
        Set<T> foundChildren = collection.stream()
                .filter(e -> equalsFunc.apply(current.get(), e))
                .collect(Collectors.toSet());
        // 没有子节点
        if (foundChildren.size() == 0) {
            return;
        }

        collection.removeAll(foundChildren);

        Set<Node<T>> childrenNode = foundChildren.stream().map(children -> {
            Node<T> node = new Node<>();
            node.set(children);
            return node;
        }).collect(Collectors.toSet());

        Set<Node<T>> child = Optional.ofNullable(current.getChildren()).orElse(new HashSet<>());
        child.addAll(childrenNode);
        current.setChildren(child);

        for (Node<T> next : childrenNode) {
            treeChildren(next, collection, equalsFunc);
        }
    }


    /**
     * 根据数据生成树
     *
     * @param collection 数据
     * @param findParentFunc 寻找父亲节点的方法
     * @param findChildrenFunc 寻找子节点的方法
     * @return 生成的树
     * @param <T> 泛型
     */
    public static <T> List<? extends TreeNode<T>> buildTree(Collection<? extends TreeNode<T>> collection,
                                                     BiFunction<TreeNode<T>, TreeNode<T>, Boolean> findParentFunc,
                                                     BiFunction<TreeNode<T>, TreeNode<T>, Boolean> findChildrenFunc
    ) {
        if (ObjectUtils.isEmpty(collection)) {
            throw new CustomException("传入数据为空");
        }
        // parentId-child parentId 能为空
        List<TreeNode<T>> multiTree = new ArrayList<>();

        Iterator<? extends TreeNode<T>> ite = collection.iterator();
        // 注意： 不要改成 for循环，下面操作涉及集合节点删除
        while(ite.hasNext()) {
            TreeNode<T> node = ite.next();
            collection.remove(node);

            TreeNode<T> parent = treeParent(node, collection, findParentFunc);

            if (parent.equals(node)) {
                treeChildren(parent, collection, findChildrenFunc);
            } else {
                for (TreeNode<T> childrenNode : parent.getChildren()) {
                    treeChildren(childrenNode, collection, findChildrenFunc);
                }
            }
            ite = collection.stream().iterator();
            multiTree.add(parent);
        }
        return multiTree;
    }

    /**
     * 寻找当前节点的父亲节点
     *
     * @param current 当前节点
     * @param collection 数据
     * @param equalsFunc 父节点比较方法
     * @return 父亲节点
     * @param <T> 泛型
     */
    private static <T> TreeNode<T> treeParent(TreeNode<T> current, Collection<? extends TreeNode<T>> collection,
                                              BiFunction<TreeNode<T>, TreeNode<T>, Boolean> equalsFunc) {

        TreeNode<T> parentNode = collection.stream()
                .filter(p -> equalsFunc.apply(current, p)).findFirst()
                .orElse(null);

        if (parentNode == null) {
            return current;
        }

        collection.remove(parentNode);

        Collection<TreeNode<T>> child = Optional.ofNullable(parentNode.getChildren()).orElse(new HashSet<>());
        child.add(current);
        parentNode.setChildren(child);
        treeParent(parentNode, collection, equalsFunc);

        return parentNode;
    }

    /**
     * 从数据中寻找子节点并装入当前节点
     *
     * @param current 当前节点
     * @param collection 数据
     * @param equalsFunc 子节点比较方法
     * @param <T> 泛型
     */
    private static <T> void treeChildren(TreeNode<T> current, Collection<? extends TreeNode<T>> collection,
                                         BiFunction<TreeNode<T>, TreeNode<T>, Boolean> equalsFunc
    ) {
        Set<? extends TreeNode<T>> foundChildren = collection.stream()
                .filter(e -> equalsFunc.apply(current, e))
                .collect(Collectors.toSet());
        // 没有子节点
        if (foundChildren.size() == 0) {
            return;
        }

        collection.removeAll(foundChildren);

        Collection<TreeNode<T>> child = Optional.ofNullable(current.getChildren()).orElse(new HashSet<>());
        child.addAll(foundChildren);
        current.setChildren(child);

        for (TreeNode<T> next : foundChildren) {
            treeChildren(next, collection, equalsFunc);
        }
    }


}
