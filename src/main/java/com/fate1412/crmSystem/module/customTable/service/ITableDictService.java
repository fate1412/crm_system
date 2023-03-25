package com.fate1412.crmSystem.module.customTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableDictInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.module.mainTable.dto.select.ProductSelectDTO;
import com.fate1412.crmSystem.utils.JsonResult;

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
    
    TableDict getByRealName(String realNames);
    
    MyPage listByPage(SelectPage<TableDictSelectDTO> selectPage);
    
    JsonResult<?> addDTO(TableDictInsertDTO tableDictInsertDTO);
}
