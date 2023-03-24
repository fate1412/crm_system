package com.fate1412.crmSystem.module.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableColumnInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.mapper.TableColumnDictMapper;
import com.fate1412.crmSystem.module.customTable.service.ITableColumnDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    private TableColumnDictMapper mapper;
    @Autowired
    private TableDictMapper tableDictMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<TableColumnDict> listByTableName(String tableName) {
        QueryWrapper<TableColumnDict> queryWrapper = new QueryWrapper<>();
        return mapper.selectList(queryWrapper);
    }
    
    @Override
    public List<TableColumnSelectDTO> DTOListByTableName(String tableName) {
        QueryWrapper<TableColumnDict> queryWrapper = new QueryWrapper<>();
        List<TableColumnDict> tableColumnDictList = mapper.selectList(queryWrapper);
        return MyCollections.copyListProperties(tableColumnDictList, TableColumnSelectDTO::new);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        return null;
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.tableColumnDict,new TableColumnSelectDTO(),tableOptionService);
    }
    
    @Override
    public JsonResult<?> addDTO(TableColumnInsertDTO tableColumnInsertDTO) {
        TableColumnDict tableColumnDict = new TableColumnDict();
        BeanUtils.copyProperties(tableColumnInsertDTO,tableColumnDict);
        return add(new MyEntity<TableColumnDict>(tableColumnDict) {
            @Override
            public TableColumnDict set(TableColumnDict tableColumnDict) {
                tableColumnDict.setRealColumnName(tableColumnDict.getColumnName()+"_"+ tableColumnDict.getColumnIndex());
                return tableColumnDict;
            }
        });
    }
    
    @Override
    public List<?> getDTOList(List<TableColumnDict> tableColumnDictList) {
        List<OptionDTO> columnType = tableOptionService.getOptions(TableNames.tableColumnDict, "columnType");
        Map<Integer, String> optionMap = MyCollections.list2MapL(columnType, OptionDTO::getOptionKey, OptionDTO::getOption);
    
        List<TableColumnSelectDTO> dtoList = MyCollections.copyListProperties(tableColumnDictList, TableColumnSelectDTO::new);
        dtoList.forEach(dto -> {
            dto.setColumnTypeR(optionMap.get(dto.getColumnType()));
        });
        return dtoList;
    }
    
    @Override
    public BaseMapper<TableColumnDict> mapper() {
        return mapper;
    }
    
    @Override
    public MyPage listByPage(SelectPage<TableDictSelectDTO> selectPage) {
        TableDictSelectDTO like = selectPage.getLike();
        QueryWrapper<TableColumnDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TableColumnDict::getTableName,like.getTableName())
                .like(like.getShowName() != null,TableColumnDict::getShowName,like.getShowName());
        return listByPage(1,1000,queryWrapper);
    }
}
