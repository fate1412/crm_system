package com.fate1412.crmSystem.customTable.service;

import com.fate1412.crmSystem.customTable.dto.TableColumn;
import com.fate1412.crmSystem.customTable.dto.TableDictDTO;

import java.util.List;

public interface ICustomTableService {
    
    List<TableDictDTO> getTables();
    
    List<TableColumn> getTableColumns(String tableName);
}