package com.fate1412.crmSystem.moduel.customTable.service;

import com.fate1412.crmSystem.moduel.customTable.dto.TableColumnDTO;
import com.fate1412.crmSystem.moduel.customTable.dto.TableDictDTO;

import java.util.List;

public interface ICustomTableService {
    
    List<TableDictDTO> getTables();
    
    List<TableColumnDTO> getTableColumns(String tableName);
}
