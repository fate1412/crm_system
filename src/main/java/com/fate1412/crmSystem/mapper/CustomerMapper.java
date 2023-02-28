package com.fate1412.crmSystem.mapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.pojo.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.utils.SQLFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    
    List<JSONObject> selectAll(@Param("tableName") String tableName ,@Param("factors") List<SQLFactor<?>> factors);
    String selectAll2(String tableName, SQLFactor factor);
    
    @Select("select * from #{tableName}")
    JSONArray selectAll3(@Param("tableName") String tableName, SQLFactor factor);
}
