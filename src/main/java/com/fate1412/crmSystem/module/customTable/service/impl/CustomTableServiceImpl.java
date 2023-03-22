package com.fate1412.crmSystem.module.customTable.service.impl;

import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.module.customTable.service.ITableColumnDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
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
    public List<TableDictSelectDTO> getTables() {
        List<TableDict> list = tableDictService.list();
        return MyCollections.copyListProperties(list, TableDictSelectDTO::new);
    }
    
    @Override
    public List<TableColumnSelectDTO> getTableColumns(String tableName) {
        List<TableColumnDict> tableColumnDictList = tableColumnDictService.listByTableName(tableName);
        log.info("-----------------------");
        List<TableColumnSelectDTO> dtoList = new ArrayList<>();
        tableColumnDictList.forEach(tableColumnDict -> {
            TableColumnSelectDTO dto = new TableColumnSelectDTO();
            BeanUtils.copyProperties(tableColumnDict,dto);
            //Select类型
            if (tableColumnDict.getColumnType().equals(FormType.Select.getIndex())) {
                List<OptionDTO> optionDTOS = tableOptionService.getOptions(tableName, tableColumnDict.getColumnName());
//                dto.setOptions(optionDTOS);
            }
            dtoList.add(dto);
        });
        return dtoList;
    }
}
