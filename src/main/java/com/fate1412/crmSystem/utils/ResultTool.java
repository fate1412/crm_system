package com.fate1412.crmSystem.utils;

import com.fate1412.crmSystem.annotations.MapKey;
import com.fate1412.crmSystem.annotations.TableTitle;
import org.apache.logging.log4j.util.Strings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            tableColumnList.add(getTableColumn(title, fieldName));
        }
        return new TableResultData(tableColumnList, entities);
        
    }
}
