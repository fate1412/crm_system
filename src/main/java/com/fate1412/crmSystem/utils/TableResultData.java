package com.fate1412.crmSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class TableResultData {
    private List<TableColumn> tableColumns;
    private List<?> tableDataList;
    private Long thisPage;
    private Long lastPage;
    
    public TableResultData(List<TableColumn> tableColumns, List<?> tableDataList) {
        this.tableColumns = tableColumns;
        this.tableDataList = tableDataList;
    }
    
    @Data
    @AllArgsConstructor
    static class TableColumn {
        private String title;
        private String name;
    }
}
