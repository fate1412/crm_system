package com.fate1412.crmSystem.module.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private ITableOptionService tableOptionService;
    
    
    @Override
    public List<TableDict> getByTableName(List<String> tableNames) {
        if (MyCollections.isEmpty(tableNames)) {
            return new ArrayList<>();
        }
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(TableDict::getTableName, tableNames);
        return list(queryWrapper);
    }
    
    @Override
    public MyPage listByPage(SelectPage<TableDictSelectDTO> selectPage) {
        TableDictSelectDTO like = selectPage.getLike();
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(like.getShowName()!= null,TableDict::getShowName,like.getShowName());
        return listByPage(1,1000,queryWrapper);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(TableDict::getId, TableDict::getShowName)
                .like(TableDict::getShowName, nameLike);
        IPage<TableDict> iPage = new Page<>(page, 10);
        mapper.selectPage(iPage, queryWrapper);
        return IdToName.createList(iPage.getRecords(), TableDict::getId, TableDict::getShowName);
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.tableDict,new TableDictSelectDTO(),tableOptionService);
    }
    
    @Override
    public List<?> getDTOList(List<TableDict> tableDictList) {
        return MyCollections.copyListProperties(tableDictList,TableDictSelectDTO::new);
    }
    
    @Override
    public BaseMapper<TableDict> mapper() {
        return mapper;
    }
}
