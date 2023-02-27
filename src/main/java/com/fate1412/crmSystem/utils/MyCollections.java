package com.fate1412.crmSystem.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.annotations.MapKey;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
     * List对象转Map(全部存入List)
     */
    public static <K, T> Map<K, List<T>> list2MapList(List<T> list, Function<? super T, ? extends K> keyMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, MyCollections::toList, MyCollections::addList));
    }
    
    /**
     * 合并集合并返回新集合
     */
    public static <T> List<T> addList(List<T> l1, List<T> l2) {
        List<T> list = new ArrayList<>();
        list.addAll(l1);
        list.addAll(l2);
        return list;
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
     * 为'空'的对象返回null
     */
    public static <T> T getData(T o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            String s = o.toString();
            return s.equals("") ? null : o;
        }
        if (o instanceof List) {
            List<?> list = (List<?>) o;
            return isEmpty(list) ? null : o;
        }
        if (o instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) o;
            return isEmpty(map) ? null : o;
        }
        return o;
    }
    
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
