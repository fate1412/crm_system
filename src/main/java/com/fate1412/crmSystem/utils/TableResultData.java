package com.fate1412.crmSystem.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.annotations.TableTitle;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    
    public static <T> List<TableColumn> tableColumnList(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        List<TableColumn> tableColumnList = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            TableTitle tableTitle = field.getAnnotation(TableTitle.class);
            
            String fieldName = field.getName();
            if (tableTitle == null) {
                continue;
            }
            String title = tableTitle.value();
            TableColumn tableColumn = new TableColumn(title, fieldName);
            tableColumn
                    .setFixed(tableTitle.fixed())
                    .setLink(tableTitle.link())
                    .setDisabled(tableTitle.disabled())
                    .setFormType(tableTitle.formType());
            
            tableColumnList.add(tableColumn);
        }
        return tableColumnList;
    }
    
    public static <T> TableResultData createTableResultData(List<?> entities, Class<T> entityClass) {
        List<TableColumn> tableColumnList = tableColumnList(entityClass);
        return new TableResultData(tableColumnList, entities);
        
    }
    
    public static <T> TableResultData createTableResultData(List<?> entities, Class<T> entityClass, long thisPage, long total) {
        TableResultData tableResultData = createTableResultData(entities, entityClass);
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(total);
        return tableResultData;
    }
    
    public static <T> TableResultData createTableResultData(IPage<T> page, Class<T> entityClass) {
        return createTableResultData(page.getRecords(),entityClass, page.getPages(), page.getTotal());
    }
}
