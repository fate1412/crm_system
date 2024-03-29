package com.fate1412.crmSystem.module.customTable.mapper;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.utils.SQLFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据库表字典表 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Mapper
public interface TableDictMapper extends BaseMapper<TableDict> {
    List<JSONObject> select(@Param("tableName") String tableName, @Param("factors") List<SQLFactor<Object>> factors);
    
    Integer insertList(@Param("tableName") String tableName, @Param("dataList") List<List<SQLFactor<Object>>> dataList);
    
    Integer updateList(@Param("tableName") String tableName, @Param("dataList") List<List<SQLFactor<Object>>> dataList);
    
    Integer realDeleteList(@Param("tableName") String tableName, @Param("factors") List<SQLFactor<Object>> factors);
    
    Integer deleteList(@Param("tableName") String tableName, @Param("factors") List<SQLFactor<Object>> factors);
    
    Integer createTable(@Param("tableDict") TableDict tableDict);
    
    Integer createColumn(@Param("tableName") String tableName, @Param("columnDict") TableColumnDict columnDict);
    
    Integer delColumn(@Param("tableName") String tableName, @Param("columnName") String columnName);
    
    Integer delTable(@Param("tableName") String tableName);
}
