package com.fate1412.crmSystem.customTable.service.impl;

import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.customTable.dto.TableColumn;
import com.fate1412.crmSystem.customTable.dto.TableDictDTO;
import com.fate1412.crmSystem.customTable.pojo.Option;
import com.fate1412.crmSystem.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.customTable.pojo.TableDict;
import com.fate1412.crmSystem.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.customTable.service.ITableColumnDictService;
import com.fate1412.crmSystem.customTable.service.ITableDictService;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.utils.MyCollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomTableServiceImpl implements ICustomTableService {
    @Autowired
    private ITableDictService tableDictService;
    @Autowired
    private ITableColumnDictService tableColumnDictService;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<TableDictDTO> getTables() {
        List<TableDict> list = tableDictService.list();
        return MyCollections.copyListProperties(list,TableDictDTO::new);
    }
    
    @Override
    public List<TableColumn> getTableColumns(String tableName) {
        List<TableColumnDict> tableColumnDictList = tableColumnDictService.listByTableName(tableName);
        log.info("-----------------------");
        List<TableColumn> dtoList = new ArrayList<>();
        tableColumnDictList.forEach(tableColumnDict -> {
            TableColumn dto = new TableColumn();
            BeanUtils.copyProperties(tableColumnDict,dto);
            //Select类型
            if (tableColumnDict.getColumnType().equals(FormType.Select.getIndex())) {
                List<Option> options = tableOptionService.getOptions(tableName, tableColumnDict.getColumnName());
                dto.setColumnType(options);
            }
            dtoList.add(dto);
        });
        return dtoList;
    }
}