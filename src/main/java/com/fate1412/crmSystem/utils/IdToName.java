package com.fate1412.crmSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdToName {
    private Long id;
    private String name;
    private String tableName;
    
    public IdToName(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public IdToName(String tableName) {
        this.tableName = tableName;
    }
    
    public static <T> IdToName create(T t, Function<? super T, ? extends Long> idMapper, Function<? super T, ? extends String> nameMapper) {
        return new IdToName(idMapper.apply(t), nameMapper.apply(t));
    }
    
    public static <T> IdToName create2(T t, Function<? super T, ? extends Long> idMapper, Function<? super T, ? extends Long> nameMapper) {
        return new IdToName(idMapper.apply(t), nameMapper.apply(t).toString());
    }
    
    public static <T> List<IdToName> createList(List<T> list, Function<? super T, ? extends Long> idMapper, Function<? super T, ? extends String> nameMapper) {
        return list.stream().map(t -> create(t, idMapper,nameMapper)).collect(Collectors.toList());
    }
    
    public static <T> List<IdToName> createList2(List<T> list, Function<? super T, ? extends Long> idMapper, Function<? super T, ? extends Long> nameMapper) {
        return list.stream().map(t -> create2(t, idMapper,nameMapper)).collect(Collectors.toList());
    }
}
