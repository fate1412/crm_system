package com.fate1412.crmSystem.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class SQLFactor<T> {
    private String TableName;
    private String field;
    private String factor;
    private T value;
    private T value2;
    private List<T> values;
    
    
    public SQLFactor(String factor) {
        this.factor = factor;
    }
    
    public SQLFactor(String field, String factor, T value) {
        this.field = field;
        this.factor = factor;
        this.value = value;
    }
    
    public SQLFactor(String field, String factor, T value, T value2) {
        this.field = field;
        this.factor = factor;
        this.value = value;
        this.value2 = value2;
    }
    
    public SQLFactor(String field, String factor, List<T> values) {
        this.field = field;
        this.factor = factor;
        this.values = values;
    }
    
    /**
     * 查询条件注入queryWrapper
     * @param factors 查询条件
     * @param queryWrapper
     */
    public static void toQueryWrapper(List<SQLFactor<Object>> factors, QueryWrapper<?> queryWrapper) {
        if (factors == null) {
            factors = new ArrayList<>();
        }
        for (SQLFactor<Object> factor : factors) {
            setQW(factor, queryWrapper);
        }
    }
    
    private static void setQW(SQLFactor<Object> factor, QueryWrapper<?> queryWrapper) {
        switch (factor.factor) {
            case "EQ": queryWrapper.eq(factor.field, factor.value); break;
            case "NE": queryWrapper.ne(factor.field, factor.value); break;
            case "GT": queryWrapper.gt(factor.field, factor.value); break;
            case "GE": queryWrapper.ge(factor.field, factor.value); break;
            case "LT": queryWrapper.lt(factor.field, factor.value); break;
            case "LE": queryWrapper.le(factor.field, factor.value); break;
            case "LIKE": queryWrapper.like(factor.field, factor.value); break;
            case "LEFT_LIKE": queryWrapper.likeLeft(factor.field, factor.value); break;
            case "RIGHT_LIKE": queryWrapper.likeRight(factor.field, factor.value); break;
            case "NOT_LIKE": queryWrapper.notLike(factor.field, factor.value); break;
            case "BETWEEN": queryWrapper.between(factor.field, factor.value, factor.value2); break;
            case "NOT_BETWEEN": queryWrapper.notBetween(factor.field, factor.value, factor.value2); break;
            case "ISNULL": queryWrapper.isNull(factor.field); break;
            case "IS_NOTNULL": queryWrapper.isNotNull(factor.field); break;
            case "IN": queryWrapper.in(factor.field, factor.values); break;
            case "NOT_IN": queryWrapper.notIn(factor.field, factor.values); break;
            case "OR": queryWrapper.or(); break;
        }
    }
    
    /**
     * 使用DTO创建QueryWrapper(IN查询)；
     * 用于查询的属性名优先使用(TableId，TableField)注解；
     * 属性的值作为查询条件，null不作为查询条件；
     * @param dataDTO DTO
     * @param EntityClazz 类型
     * @param useAnnotation 开启时仅有使用注解的属性可作为条件，默认开启
     */
    public static <D, T> QueryWrapper<T> data2QueryWrapper(D dataDTO, Class<T> EntityClazz, boolean useAnnotation) {
        if (dataDTO == null) return null;
        Class<?> dataClass = dataDTO.getClass();
        Field[] fields = dataClass.getDeclaredFields();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(dataDTO);
                if (obj != null) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    String fieldName = null;
                    if (tableField != null) {
                        fieldName = tableField.value();
                    }
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableId != null) {
                        fieldName = tableId.value();
                    }
                    if (!useAnnotation || fieldName == null) {
                        fieldName = field.getName();
                    }
                    if (obj instanceof List) {
                        List<Object> list = new ArrayList<>((List<?>) obj);
                        queryWrapper.in(fieldName, list);
                    } else {
                        queryWrapper.in(fieldName, obj);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return queryWrapper;
    }
    
    /**
     * 使用DTO创建QueryWrapper(IN查询)；
     * 用于查询的属性必需要使用(TableId，TableField)注解；
     * 属性的值作为查询条件，null不作为查询条件
     * @param dataDTO DTO
     * @param EntityClazz 类型
     */
    public static <D, T> QueryWrapper<T> data2QueryWrapper(D dataDTO, Class<T> EntityClazz) {
        return data2QueryWrapper(dataDTO,EntityClazz,true);
    }
    
    /**
     * 默认page=1,pageSize=500
     */
    public static <T> IPage<T> getPage(Long page, Long pageSize, Class<T> EntityClazz) {
        page = (page == null || page <= 0) ? 1 : page;
        pageSize = (pageSize == null || pageSize <= 0) ? 500 : pageSize;
        return new Page<T>(page, pageSize);
    }
    
}
