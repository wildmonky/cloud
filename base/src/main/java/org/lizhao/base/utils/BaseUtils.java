package org.lizhao.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
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
}
