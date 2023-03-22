package com.fate1412.crmSystem.module.customTable.service;

import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;

import java.util.List;

public interface ICustomTableService {
    
    List<TableDictSelectDTO> getTables();
    
    List<TableColumnSelectDTO> getTableColumns(String tableName);
}
