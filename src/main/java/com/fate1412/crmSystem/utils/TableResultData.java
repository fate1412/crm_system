package com.fate1412.crmSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
public class TableResultData {
    private List<TableColumn> tableColumns;
    private List<?> tableDataList;
    private Long thisPage;
    private Long total;
    
    public TableResultData(List<TableColumn> tableColumns, List<?> tableDataList) {
        this.tableColumns = tableColumns;
        this.tableDataList = tableDataList;
    }
    
    @Data
    @AllArgsConstructor
    @Accessors(chain = true)
    static class TableColumn {
        private String title;
        private String name;
        private Boolean fixed = false;
        private Boolean link = false;
    
        public TableColumn(String title, String name) {
            this.title = title;
            this.name = name;
        }
    }
}
