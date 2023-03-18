package com.fate1412.crmSystem.moduel.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.moduel.customTable.pojo.TableDict;
import com.fate1412.crmSystem.moduel.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.moduel.customTable.service.ITableDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据库表字典表 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Service
public class TableDictServiceImpl extends ServiceImpl<TableDictMapper, TableDict> implements ITableDictService {
    @Autowired
    private TableDictMapper mapper;
    
    
    @Override
    public List<TableDict> getByTableName(List<String> tableNames) {
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(TableDict::getTableName, tableNames);
        return list(queryWrapper);
    }
    
}
