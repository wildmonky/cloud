package org.lizhao.user.handler;

import org.apache.commons.lang3.ObjectUtils;
import org.lizhao.base.entity.relation.GroupUserRelation;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Description 通用 处理器
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-04-07 22:23
 * @since 0.0.1-SNAPSHOT
 */
public class CommonHandler {

    /**
     * buildFunc 根据传入 map 生成绑定关系，并通过 saveFunc保存
     *
     * @param map 要绑定的关系Map
     * @param buildFunc 构建对应关系的方法
     * @param saveFunc 保存关系的方法
     * @return 保存方法执行结果
     * @param <T> 传入参数Map中Key的具体类型
     * @param <E> 传入参数Map中Value（Collection）中的参数类型
     * @param <S> T-E 之间的关系类，例如：{@link GroupUserRelation}
     * @param <R> saveFunc执行结果
     */
    public static <T, E, S, R> Publisher<R> bind(Map<T, Collection<E>> map,
                                              BiFunction<T, E, S> buildFunc,
                                              Function<Publisher<S>, Publisher<R>> saveFunc
    ) {
        if (ObjectUtils.isEmpty(map)) {
            return Flux.empty();
        }

        Flux<S> relationFlux = Flux.push(fluxSink -> {
            for (Map.Entry<T, Collection<E>> entry : map.entrySet()) {
                T t = entry.getKey();
                if (t == null) {
                    continue;
                }
                Collection<E> es = entry.getValue();
                if (es == null) {
                    continue;
                }
                for(E e: es) {
                    if (e == null) {
                        continue;
                    }
                    S s = buildFunc.apply(t,e);
                    if (s != null) {
                        fluxSink.next(s);
                    }
                }
                fluxSink.complete();
            }
        });
        return saveFunc.apply(relationFlux);
    }

}
