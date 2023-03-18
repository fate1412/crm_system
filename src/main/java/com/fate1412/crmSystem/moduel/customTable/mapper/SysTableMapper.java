package com.fate1412.crmSystem.moduel.customTable.mapper;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.moduel.customTable.pojo.SysTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.utils.SQLFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 表/字段名字典表 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-02-28
 */
@Mapper
public interface SysTableMapper extends BaseMapper<SysTable> {
    
    List<JSONObject> select(@Param("tableName") String tableName , @Param("factors") List<SQLFactor<?>> factors);
}
