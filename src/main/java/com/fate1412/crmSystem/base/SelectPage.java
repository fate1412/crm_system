package com.fate1412.crmSystem.base;

import lombok.Data;

@Data
public class SelectPage<T> {
    Long page = 1L;
    Long pageSize = 10L;
    T like;
    String tableName;
    
    
}
