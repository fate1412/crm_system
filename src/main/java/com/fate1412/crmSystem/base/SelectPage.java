package com.fate1412.crmSystem.base;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SelectPage<T> {
    Long page = 1L;
    Long pageSize = 10L;
    T like;
    String tableName;
    
    public void setTableName(String tableName) {
        if (StringUtils.isNotBlank(tableName)) {
            this.tableName = tableName.trim();
        }
    }
}
