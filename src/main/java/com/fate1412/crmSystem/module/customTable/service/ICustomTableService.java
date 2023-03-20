package com.fate1412.crmSystem.module.customTable.service;

import com.fate1412.crmSystem.module.customTable.dto.TableColumnDTO;
import com.fate1412.crmSystem.module.customTable.dto.TableDictDTO;

import java.util.List;

public interface ICustomTableService {
    
    List<TableDictDTO> getTables();
    
    List<TableColumnDTO> getTableColumns(String tableName);
}
