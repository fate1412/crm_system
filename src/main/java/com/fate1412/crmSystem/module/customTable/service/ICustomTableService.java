package com.fate1412.crmSystem.module.customTable.service;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.select.CustomTableSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.utils.CustomTableResultData;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.TableResultData;

import java.util.List;

public interface ICustomTableService {
    
    List<CustomTableSelectDTO> getTables();
    
    CustomTableResultData getTableColumns(String tableName);
    
    CustomTableResultData listByPage(SelectPage<JSONObject> selectPage);
    
    CustomTableResultData getDTOListById(String tableName, List<Object> idList);
    
    JsonResult<?> addDTO(JSONObject jsonObject);
    
    JsonResult<?> updateDTO(JSONObject jsonObject);
}
