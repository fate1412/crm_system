package com.fate1412.crmSystem.module.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.pojo.TableOption;
import com.fate1412.crmSystem.module.customTable.mapper.TableOptionMapper;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.MyCollections;
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
}
