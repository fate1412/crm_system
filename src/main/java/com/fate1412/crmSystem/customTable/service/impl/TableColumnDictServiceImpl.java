package com.fate1412.crmSystem.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.customTable.dto.TableColumn;
import com.fate1412.crmSystem.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.customTable.mapper.TableColumnDictMapper;
import com.fate1412.crmSystem.customTable.service.ITableColumnDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.MyCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字段字典表 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Service
public class TableColumnDictServiceImpl extends ServiceImpl<TableColumnDictMapper, TableColumnDict> implements ITableColumnDictService {
    @Autowired
    private TableColumnDictMapper tableColumnDictMapper;
    @Autowired
    private TableDictMapper tableDictMapper;
    
    @Override
    public List<TableColumnDict> listByTableName(String tableName) {
        QueryWrapper<TableColumnDict> queryWrapper = new QueryWrapper<>();
        return tableColumnDictMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<TableColumn> DTOListByTableName(String tableName) {
        QueryWrapper<TableColumnDict> queryWrapper = new QueryWrapper<>();
        List<TableColumnDict> tableColumnDictList = tableColumnDictMapper.selectList(queryWrapper);
        return MyCollections.copyListProperties(tableColumnDictList, TableColumn::new);
    }
}
