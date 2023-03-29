package com.fate1412.crmSystem.module.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableOptionSelectDTO;
import com.fate1412.crmSystem.module.customTable.mapper.TableColumnDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableOption;
import com.fate1412.crmSystem.module.customTable.mapper.TableOptionMapper;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableColumn;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字段选择值 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Service
public class TableOptionServiceImpl extends ServiceImpl<TableOptionMapper, TableOption> implements ITableOptionService {
    @Autowired
    private TableOptionMapper mapper;
    @Autowired
    private TableColumnDictMapper columnDictMapper;
    
    @Override
    public List<OptionDTO> getOptions(String tableName, String columnName) {
        QueryWrapper<TableOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TableOption::getTableName,tableName)
                .eq(TableOption::getColumnName,columnName);
        List<TableOption> tableOptions = mapper.selectList(queryWrapper);
        return MyCollections.copyListProperties(tableOptions, OptionDTO::new);
    }
    
    @Override
    public boolean selectOptions(String tableName, String columnName, Integer key) {
        QueryWrapper<TableOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TableOption::getTableName,tableName)
                .eq(TableOption::getColumnName,columnName)
                .eq(TableOption::getOptionKey,key);
        List<TableOption> tableOptions = mapper.selectList(queryWrapper);
        return !MyCollections.isEmpty(tableOptions);
    }
    
    @Override
    public List<TableOptionSelectDTO> getDTOByTableColumnId(Integer id) {
        TableColumnDict tableColumnDict = columnDictMapper.selectById(id);
        QueryWrapper<TableOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TableOption::getTableName,tableColumnDict.getTableName())
                .eq(TableOption::getColumnName,tableColumnDict.getColumnName());
        List<TableOption> tableOptions = mapper.selectList(queryWrapper);
        return MyCollections.copyListProperties(tableOptions,TableOptionSelectDTO::new);
    }
    
    @Override
    public <D> TableResultData getColumns(D dto) {
        List<TableColumn> tableColumnList = TableResultData.tableColumnList(dto.getClass());
        TableResultData tableResultData = new TableResultData();
        tableResultData.setTableColumns(tableColumnList);
        tableResultData.setTableDataList(MyCollections.toList(dto));
        return tableResultData;
    }
    
    @Override
    public boolean delAllOption(String tableName) {
        QueryWrapper<TableOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TableOption::getTableName,tableName);
        remove(queryWrapper);
        List<TableOption> list = list(queryWrapper);
        return MyCollections.isEmpty(list);
    }
}
