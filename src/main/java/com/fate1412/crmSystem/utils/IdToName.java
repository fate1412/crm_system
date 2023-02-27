package com.fate1412.crmSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Function;

@Data
@AllArgsConstructor
public class IdToName {
    private Long id;
    private String name;
    private String tableName;
    
    public IdToName(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public static <T> IdToName create(T t, Function<? super T, ? extends Long> idMapper, Function<? super T, ? extends String> nameMapper) {
        return new IdToName(idMapper.apply(t), nameMapper.apply(t));
    }
}
