package com.fate1412.crmSystem.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.annotations.TableTitle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResultTool {
    public static JsonResult<?> success() {
        return new JsonResult<>(true);
    }
    
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(true, data);
    }
    
    public static JsonResult<?> fail() {
        return new JsonResult<>(false);
    }
    
    public static JsonResult<?> fail(ResultCode resultEnum) {
        return new JsonResult<>(false, resultEnum);
    }
    
    public static JsonResult<?> create(Boolean b) {
        if (b) {
            return success();
        }
        return fail();
    }
    
    public static TableResultData.TableColumn getTableColumn(String title, String name) {
        return new TableResultData.TableColumn(title, name);
    }
    
    public static <T> TableResultData createTableResultData(List<T> entities, Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        List<TableResultData.TableColumn> tableColumnList = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            TableTitle tableTitle = field.getAnnotation(TableTitle.class);
            
            String fieldName = field.getName();
            if (tableTitle == null) {
                continue;
            }
            String title = tableTitle.value();
            TableResultData.TableColumn tableColumn = getTableColumn(title, fieldName);
            tableColumn
                    .setFixed(tableTitle.fixed())
                    .setLink(tableTitle.link())
                    .setDisabled(tableTitle.disabled())
                    .setFormType(tableTitle.formType());
                    
            tableColumnList.add(tableColumn);
        }
        return new TableResultData(tableColumnList, entities);
        
    }
    
    public static <T> TableResultData createTableResultData(List<T> entities, Class<T> entityClass, long thisPage, long total) {
        TableResultData tableResultData = createTableResultData(entities, entityClass);
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(total);
        return tableResultData;
    }
    
    public static <T> TableResultData createTableResultData(IPage<T> page,Class<T> entityClass) {
        return createTableResultData(page.getRecords(),entityClass, page.getPages(), page.getTotal());
    }
}
