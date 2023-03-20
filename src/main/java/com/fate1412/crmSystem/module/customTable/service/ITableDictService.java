package com.fate1412.crmSystem.module.customTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据库表字典表 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
public interface ITableDictService extends IService<TableDict>, MyBaseService<TableDict> {
    
    List<TableDict> getByTableName(List<String> tableNames);
    
    
}
