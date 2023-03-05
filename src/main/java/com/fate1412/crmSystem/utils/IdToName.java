package com.fate1412.crmSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdToName {
    private Long value;
    private String title;
    private String tableName;
    
    public IdToName(Long value, String title) {
        this.value = value;
        this.title = title;
    }
    
    public static <T> IdToName create(T t, Function<? super T, ? extends Long> idMapper, Function<? super T, ? extends String> nameMapper) {
        return new IdToName(idMapper.apply(t), nameMapper.apply(t));
    }
}
