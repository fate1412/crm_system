package com.fate1412.crmSystem.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.annotations.MapKey;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyCollections {
    
    /**
     * @param nonNull 是否 value不能为空
     * @param map     map集合
     * @param key     键
     * @param value   值
     */
    private static <K, V, T extends V> void mapPut(boolean nonNull, Map<K, V> map, K key, T value) {
        if (nonNull && value != null) {
            map.put(key, value);
        } else if (!nonNull) {
            map.put(key, value);
        }
    }
    
    /**
     * value为空时不插入
     *
     * @param map   map集合
     * @param key   键
     * @param value 值
     */
    public static <K, V, T extends V> void mapPut(Map<K, V> map, K key, T value) {
        mapPut(true, map, key, value);
    }
    
    /**
     * 变化list集合
     *
     * @param objects 对象集合
     * @param mapper  方法
     * @param b       是否去重
     */
    public static <T, R> List<R> objects2List(List<T> objects, Function<? super T, ? extends R> mapper, boolean b) {
        if (b) {
            return objects.stream().filter(Objects::nonNull).map(mapper).distinct().collect(Collectors.toList());
        } else {
            return objects.stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
        }
    }
    
    /**
     * 变化list集合(去重)
     *
     * @param objects 对象集合
     * @param mapper  方法
     */
    public static <T, R> List<R> objects2List(List<T> objects, Function<? super T, ? extends R> mapper) {
        return objects2List(objects, mapper, true);
    }
    
    /**
     * 去空去重
     *
     * @param objects 对象集合
     */
    public static <T> List<T> distinctAndNonNull(List<T> objects) {
        return objects.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }
    
    /**
     * 是否为空集合
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }
    
    /**
     * 是否为空集合
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.size() == 0;
    }
    
    /**
     * 转化为List集合
     */
    public static <T> List<T> toList(T object) {
        List<T> list = new ArrayList<>();
        list.add(object);
        return list;
    }
    
    /**
     * map转List集合
     */
    public static <K,V> List<V> map2list(Map<K,V> map) {
        List<V> list = new ArrayList<>();
        map.forEach((k,v)-> {
            list.add(v);
        });
        return list;
    }
    
    /**
     * List对象转Map(重复的取第一个)
     */
    public static <K, V, T> Map<K, V> list2MapL(List<T> list, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1));
    }
    
    /**
     * List对象转Map(重复的取最后一个)
     */
    public static <K, V, T> Map<K, V> list2MapR(List<T> list, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1));
    }
    
    /**
     * List对象转Map(重复的自定义操作)
     */
    public static <K, V, T> Map<K, V> list2Map(List<T> list, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }
    
    /**
     * List对象转Map(重复的取第一个)
     */
    public static <K, T> Map<K, T> list2MapL(List<T> list, Function<? super T, ? extends K> keyMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, t -> t, (v1, v2) -> v1));
    }
    
    /**
     * List对象转Map(重复的取最后一个)
     */
    public static <K, T> Map<K, T> list2MapR(List<T> list, Function<? super T, ? extends K> keyMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, t -> t, (v1, v2) -> v2));
    }
    
    
    /**
     * List对象转Map(重复的自定义操作)
     */
    public static <K, T> Map<K, T> list2Map(List<T> list, Function<? super T, ? extends K> keyMapper, BinaryOperator<T> mergeFunction) {
        return list.stream().collect(Collectors.toMap(keyMapper, t -> t, mergeFunction));
    }
    
    /**
     * List对象转Map(全部存入List)
     */
    public static <K, T> Map<K, List<T>> list2MapList(List<T> list, Function<? super T, ? extends K> keyMapper) {
        return list.stream().collect(Collectors.groupingBy(keyMapper));
    }
    
    /**
     * 根据指定属性取交集(集合中取list1的数据)
     */
    public static <T, R> List<T> intersection(List<T> list1, List<T> list2, Function<T, R> mapper) {
        return list2.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .filter(obj -> list1.stream().map(mapper).anyMatch(obj::equals))
                .map(obj -> list1.stream().filter(t -> mapper.apply(t).equals(obj)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据指定属性取list1中与list2不同的部分
     */
    public static <T, R> List<T> difference(List<T> list1, List<T> list2, Function<T, R> mapper) {
        Set<R> set = list2.stream().map(mapper).collect(Collectors.toSet());
        return list1.stream()
                .filter(Objects::nonNull)
                .filter(obj -> !set.contains(mapper.apply(obj)))
                .collect(Collectors.toList());
    }
    
    /**
     * 并集
     */
    public static <T> List<T> unionDistinct(List<T> list1, List<T> list2, Function<? super T, ?> keyExtractor) {
        return list1.stream()
                .flatMap(item1 -> Stream.of(item1, list2.stream()
                        .filter(item2 -> keyExtractor.apply(item1).equals(keyExtractor.apply(item2)))
                        .findFirst()
                        .orElse(null)))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 去除集合1中集合2的部分
     */
    public static <T> List<T> removeAll(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>(list1);
        list.removeAll(list2);
        return list;
    }
    
    /**
     * 合并集合并返回新集合
     */
    @SafeVarargs
    public static <T> List<T> addList(List<T>... lists) {
        return addList(false,lists);
    }
    
    /**
     * 合并集合并返回新集合（去重）
     */
    @SafeVarargs
    public static <T> List<T> addList(boolean notRepetition, List<T>... lists) {
        if (notRepetition) {
            Set<T> set = new HashSet<>();
            for (List<T> list : lists) {
                set.addAll(list);
            }
            return new ArrayList<>(set);
        } else {
            List<T> list2 = new ArrayList<>();
            for (List<T> list : lists) {
                list2.addAll(list);
            }
            return list2;
        }
    }
    
    /**
     * Map转JsonObject
     */
    public static JSONObject map2json(Map<String, ?> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(map);
        return jsonObject;
    }
    
    /**
     * 对象转JsonObject
     */
    public static JSONObject toJSONObject(Object object) {
        String json = JSONObject.toJSONString(object);
        return JSONObject.parseObject(json);
    }
    
    /**
     * List转JsonArray
     */
    public static JSONArray toJSONArray(List<?> list) {
        String json = JSONObject.toJSONString(list);
        return JSONObject.parseArray(json);
    }
    
    /**
     * 对象转Map
     * 需使用@MapKey注解
     */
    public static <T> Map<String, Object> toMap(T t) {
        Map<String, Object> map = new HashMap<>();
        Class<?> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            MapKey mapKey = field.getAnnotation(MapKey.class);
            if (mapKey == null) continue;
            try {
                String key = mapKey.value().equals("") ? field.getName() : mapKey.value();
                mapPut(mapKey.nonNull(), map, key, field.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    
    /**
     * List对象拷贝
     */
    public static <S,T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            BeanUtils.copyProperties(source,t);
            list.add(t);
        }
        return list;
    }
    
}
