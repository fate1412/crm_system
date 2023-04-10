package com.fate1412.crmSystem.module.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.module.customTable.dto.child.TableColumnChild;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableColumnInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.update.TableColumnUpdateDTO;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.mapper.TableColumnDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableOption;
import com.fate1412.crmSystem.module.customTable.service.ITableColumnDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
        queryWrapper.lambda()
                .eq(TableColumnDict::getTableName, tableName)
                .orderByAsc(TableColumnDict::getColumnIndex);
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
        return getColumns(TableNames.tableColumnDict, new TableColumnSelectDTO(), tableOptionService);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(TableColumnInsertDTO tableColumnInsertDTO) {
        TableColumnDict tableColumnDict = new TableColumnDict();
        BeanUtils.copyProperties(tableColumnInsertDTO, tableColumnDict);
        return add(new MyEntity<TableColumnDict>(tableColumnDict) {
            @Override
            public ResultCode verification(TableColumnDict tableColumnDict) {
                return isRight(tableColumnDict);
            }
            
            @Override
            public boolean after(TableColumnDict tableColumnDict) {
                return afterInsert(tableColumnDict);
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(TableColumnUpdateDTO tableColumnUpdateDTO) {
        TableColumnDict tableColumnDict = new TableColumnDict();
        BeanUtils.copyProperties(tableColumnUpdateDTO, tableColumnDict);
        List<TableColumnChild> childList = tableColumnUpdateDTO.getChildList();
        return update(new MyEntity<TableColumnDict>(tableColumnDict) {
            @Override
            public ResultCode verification(TableColumnDict tableColumnDict) {
                return isRight(tableColumnDict);
            }
            
            @Override
            public boolean after(TableColumnDict tableColumnDict) {
                TableColumnDict columnDict = mapper.selectById(tableColumnDict.getId());
                return afterUpdate(columnDict, childList);
            }
        });
    }
    
    @Override
    @Transactional
    public boolean delById(Integer id) {
        TableColumnDict tableColumnDict = mapper.selectById(id);
        if (!removeById(id)) {
            throw new DataCheckingException(ResultCode.COMMON_FAIL);
        }
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TableDict::getTableName, tableColumnDict.getTableName());
        TableDict tableDict = tableDictMapper.selectOne(queryWrapper);
        tableDictMapper.delColumn(tableDict.getRealTableName(), tableColumnDict.getRealColumnName());
        return true;
    }
    
    @Override
    public List<?> getDTOList(List<TableColumnDict> tableColumnDictList) {
        List<OptionDTO> columnType = tableOptionService.getOptions(TableNames.tableColumnDict, "columnType");
        Map<Integer, String> optionMap = MyCollections.list2MapL(columnType, OptionDTO::getOptionKey, OptionDTO::getOption);
        
        List<TableDict> tableDictList = tableDictMapper.selectList(null);
        Map<Long, String> tableMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getShowName);
        
        List<TableColumnSelectDTO> dtoList = MyCollections.copyListProperties(tableColumnDictList, TableColumnSelectDTO::new);
        dtoList.sort(Comparator.comparingInt(TableColumnSelectDTO::getColumnIndex));
        dtoList.forEach(dto -> {
            Long linkTable = dto.getLinkTable();
            dto.setColumnTypeR(optionMap.get(dto.getColumnType()));
            if (linkTable != null) {
                dto.setLinkTableR(new IdToName(linkTable, tableMap.get(linkTable), TableNames.tableDict));
            }
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
                .eq(TableColumnDict::getTableName, like.getTableName())
                .like(like.getShowName() != null, TableColumnDict::getShowName, like.getShowName());
        return listByPage(1, 1000, queryWrapper);
    }
    
    private ResultCode isRight(TableColumnDict tableColumnDict) {
        Integer id = tableColumnDict.getId();
        List<TableColumnDict> columnDictList = null;
        if (id == null) {
            //表名
            if (StringUtils.isBlank(tableColumnDict.getTableName())) {
                return ResultCode.PARAM_IS_BLANK;
            }
            tableColumnDict.setTableName(tableColumnDict.getTableName().trim());
            QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(TableDict::getTableName, tableColumnDict.getTableName());
            TableDict tableDict = tableDictMapper.selectOne(queryWrapper);
            if (tableDict == null) {
                return ResultCode.PARAM_NOT_VALID;
            }
            columnDictList = listByTableName(tableDict.getTableName());
            //判断是否定制表(新增情况)
            if (!tableDict.getCustom()) {
                return ResultCode.PARAM_NOT_VALID;
            }
            //字段名
            if (StringUtils.isBlank(tableColumnDict.getColumnName())) {
                return ResultCode.PARAM_IS_BLANK;
            }
            tableColumnDict.setColumnName(tableColumnDict.getColumnName().trim());
            List<String> columnNames = MyCollections.objects2List(columnDictList, TableColumnDict::getColumnName);
            if (columnNames.contains(tableColumnDict.getColumnName())) {
                return ResultCode.PARAM_REPEAT;
            }
            //真实字段名
            if (StringUtils.isBlank(tableColumnDict.getRealColumnName())) {
                return ResultCode.PARAM_IS_BLANK;
            }
            tableColumnDict.setRealColumnName(tableColumnDict.getRealColumnName().trim());
            List<String> realColumnName = MyCollections.objects2List(columnDictList, TableColumnDict::getRealColumnName);
            if (realColumnName.contains(tableColumnDict.getRealColumnName())) {
                return ResultCode.PARAM_REPEAT;
            }
            //字段类型
            if (tableColumnDict.getColumnType() == null) {
                return ResultCode.PARAM_IS_BLANK;
            }
            List<OptionDTO> columnType = tableOptionService.getOptions(TableNames.tableColumnDict, "columnType");
            Map<Integer, String> columnTypeMap = MyCollections.list2MapL(columnType, OptionDTO::getOptionKey, OptionDTO::getOption);
            if (columnTypeMap.get(tableColumnDict.getColumnType()) == null) {
                return ResultCode.PARAM_NOT_VALID;
            }
        }
        if (columnDictList == null) {
            TableColumnDict columnDict = mapper.selectById(id);
            columnDictList = listByTableName(columnDict.getTableName());
            tableColumnDict.setColumnType(columnDict.getColumnType());
            //表
            QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(TableDict::getTableName, tableColumnDict.getTableName());
            TableDict tableDict = tableDictMapper.selectOne(queryWrapper);
            if (tableDict == null) {
                return ResultCode.PARAM_NOT_VALID;
            }
            //是否定制表
            if (!tableDict.getCustom()) {
                //非定制表不修改
                tableColumnDict = columnDict;
            }
        }
        //字段展示名
        if (StringUtils.isBlank(tableColumnDict.getShowName())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        tableColumnDict.setShowName(tableColumnDict.getShowName().trim());
        //字段展示顺序
        List<Integer> indexList = MyCollections.objects2List(columnDictList, TableColumnDict::getColumnIndex);
        indexList.sort(Comparator.reverseOrder());
        if (tableColumnDict.getColumnIndex() == null) {
            tableColumnDict.setColumnIndex(indexList.get(0));
        } else {
            if (indexList.contains(tableColumnDict.getColumnIndex())) {
                Map<Integer, Integer> map = MyCollections.list2MapL(columnDictList, TableColumnDict::getId, TableColumnDict::getColumnIndex);
                if (id == null || !map.get(id).equals(tableColumnDict.getColumnIndex())) {
                    return ResultCode.PARAM_REPEAT;
                }
            }
        }
        if (tableColumnDict.getLink() && tableColumnDict.getColumnType().equals(FormType.Select.getIndex())) {
            if (tableColumnDict.getLinkTable() == null) {
                return ResultCode.PARAM_IS_BLANK;
            }
            QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(TableDict::getId, tableColumnDict.getLinkTable());
            TableDict tableDict = tableDictMapper.selectOne(queryWrapper);
            if (tableDict == null) {
                return ResultCode.PARAM_NOT_VALID;
            }
        } else {
            tableColumnDict.setLinkTable(null);
            tableColumnDict.setLink(false);
        }
        return ResultCode.SUCCESS;
    }
    
    private Boolean afterInsert(TableColumnDict tableColumnDict) {
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TableDict::getTableName, tableColumnDict.getTableName());
        TableDict tableDict = tableDictMapper.selectOne(queryWrapper);
        tableDictMapper.createColumn(tableDict.getRealTableName(), tableColumnDict);
        return true;
    }
    
    private Boolean afterUpdate(TableColumnDict tableColumnDict, List<TableColumnChild> childList) {
        //删除所有选项
        boolean b = tableOptionService.delAllOption(tableColumnDict.getTableName());
        if (b && !MyCollections.isEmpty(childList)) {
            childList.forEach(child -> {
                child.setTableName(tableColumnDict.getTableName());
                child.setColumnName(tableColumnDict.getColumnName());
            });
            List<TableOption> tableOptionList = MyCollections.copyListProperties(childList, TableOption::new);
            return tableOptionService.saveBatch(tableOptionList);
        }
        return b;
        
    }
}
