package com.fate1412.crmSystem.module.customTable.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.select.*;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.module.customTable.service.ITableColumnDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.pojo.InvoiceProduct;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import com.fate1412.crmSystem.utils.SQLFactor.SQLFactors;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomTableServiceImpl implements ICustomTableService {
    @Autowired
    private TableDictMapper mapper;
    @Autowired
    private ITableDictService tableDictService;
    @Autowired
    private ITableColumnDictService tableColumnDictService;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private ISysUserService sysUserService;
    
    @Override
    public List<CustomTableSelectDTO> getTables() {
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TableDict::getCustom, 1);
        List<TableDict> list = tableDictService.list(queryWrapper);
        return MyCollections.copyListProperties(list, CustomTableSelectDTO::new);
    }
    
    @Override
    public CustomTableResultData getTableColumns(String tableName) {
        CustomTableResultData tableResultData = new CustomTableResultData();
        //获取/判断是否为定制表
        List<TableDict> tableDictList = tableDictService.getByTableName(MyCollections.toList(tableName));
        if (MyCollections.isEmpty(tableDictList) || !tableDictList.get(0).getCustom()) {
            return tableResultData;
        }
        
        //获取字段类型
        List<OptionDTO> columnTypeList = tableOptionService.getOptions(TableNames.tableColumnDict, "columnType");
        Map<Integer, String> columnTypeMap = MyCollections.list2MapL(columnTypeList, OptionDTO::getOptionKey, OptionDTO::getOption);
        //获取表字段
        List<TableColumnDict> tableColumnDictList = tableColumnDictService.listByTableName(tableName);
        List<Long> linkTableIds = MyCollections.objects2List(tableColumnDictList, TableColumnDict::getLinkTable);
    
        List<TableDict> linkTableList = mapper.selectBatchIds(linkTableIds);
        Map<Long, String> tableMap = MyCollections.list2MapL(linkTableList, TableDict::getId, TableDict::getTableName);
        
        List<CustomTableColumnSelectDTO> dtoList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        tableColumnDictList.forEach(tableColumnDict -> {
            CustomTableColumnSelectDTO dto = new CustomTableColumnSelectDTO();
            BeanUtils.copyProperties(tableColumnDict, dto);
    
            jsonObject.put(dto.getColumnName(),null);
            //Select类型
            if (tableColumnDict.getColumnType().equals(FormType.Select.getIndex())) {
                List<OptionDTO> optionDTOS = tableOptionService.getOptions(tableName, tableColumnDict.getColumnName());
                dto.setOptions(optionDTOS);
                jsonObject.put(dto.getColumnName()+"R",new IdToName(null,null,tableMap.get(dto.getLinkTable())));
            }
            dto.setLabel(dto.getShowName());
            dto.setProp(dto.getColumnName());
            dto.setFormType(columnTypeMap.get(dto.getColumnType()));
            dtoList.add(dto);
        });
        
        tableResultData.setTableColumns(dtoList);
        tableResultData.setTableDataList(MyCollections.toList(jsonObject));
        return tableResultData;
    }
    
    
    @Override
    public CustomTableResultData listByPage(SelectPage<JSONObject> selectPage) {
        SQLFactors sqlFactors = new SQLFactors();
        JSONObject like = selectPage.getLike();
        like.forEach(sqlFactors::like);
        
        CustomTableResultData tableResultData = getTableColumns(selectPage.getTableName());
        MyPage page = listByPage(selectPage.getPage().intValue(), selectPage.getPageSize().intValue(), selectPage.getTableName(), sqlFactors.getSqlFactors(), tableResultData.getTableColumns());
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return tableResultData;
    }
    
    @Override
    public CustomTableResultData getDTOListById(String tableName, List<Object> idList) {
        //获取/判断是否为定制表
        List<TableDict> tableDictList = tableDictService.getByTableName(MyCollections.toList(tableName));
        if (MyCollections.isEmpty(tableDictList) || !tableDictList.get(0).getCustom()) {
            return new CustomTableResultData();
        }
        TableDict tableDict = tableDictList.get(0);
        CustomTableResultData tableResultData = getTableColumns(tableName);
        SQLFactors sqlFactors = new SQLFactors();
        sqlFactors.in("id",idList);
        List<JSONObject> jsonObjectList = mapper.select(tableDict.getRealTableName(), sqlFactors.getSqlFactors());
        List<JSONObject> dtoList = getDTOList(jsonObjectList, tableResultData.getTableColumns(), true);
        tableResultData.setTableDataList(dtoList);
        return tableResultData;
    }
    
    private MyPage listByPage(int thisPage, int pageSize, String tableName, List<SQLFactor<Object>> sqlFactors, List<CustomTableColumnSelectDTO> columnList) {
        MyPage myPage = new MyPage(thisPage, pageSize);
        //获取/判断是否为定制表
        List<TableDict> tableDictList = tableDictService.getByTableName(MyCollections.toList(tableName));
        if (MyCollections.isEmpty(tableDictList) || !tableDictList.get(0).getCustom()) {
            return myPage;
        }
        
        PageInfo<JSONObject> pageInfo = PageHelper.startPage(thisPage, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                mapper.select(tableName, sqlFactors);
            }
        });
        
        List<JSONObject> dtoList = getDTOList(pageInfo.getList(), columnList, false);
        
        myPage.setRecords(dtoList);
        myPage.setCurrent(pageInfo.getPageNum());
        myPage.setTotal(pageInfo.getTotal());
        return myPage;
    }
    
    private List<JSONObject> getDTOList(List<JSONObject> jsonList, List<CustomTableColumnSelectDTO> columnList, boolean one) {
        if (MyCollections.isEmpty(jsonList)) {
            return new ArrayList<>();
        }
        //不使用真实的字段名
        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonList.forEach(json -> {
            JSONObject jsonObject = new JSONObject();
            columnList.forEach(column -> {
                jsonObject.put(column.getColumnName(), json.get(column.getRealColumnName()));
            });
            jsonObjectList.add(jsonObject);
        });
        
        
        //用户
        List<Long> createIds = MyCollections.objects2List(jsonObjectList, (jsonObject -> jsonObject.getLong("creater")));
        List<Long> updateMemberIds = MyCollections.objects2List(jsonObjectList, (jsonObject -> jsonObject.getLong("updater")));
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
        
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        jsonObjectList.forEach(object -> {
            Long creater = object.getLong("creater");
            Long updater = object.getLong("updater");
            //多条查询时覆盖原值
            if (one) {
                object.put("createrR", new IdToName(creater, userMap.get(creater), TableNames.sysUser));
                object.put("updaterR", new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            } else {
                object.put("creater", new IdToName(creater, userMap.get(creater), TableNames.sysUser));
                object.put("updater", new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            }
        });
        //获取可链接的相关表
        List<Long> tableIds = MyCollections.objects2List(columnList, CustomTableColumnSelectDTO::getLinkTable);
        List<TableDict> tableDictList = mapper.selectBatchIds(tableIds);
        Map<Long, String> realTableMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getRealTableName);
        Map<Long, String> tableMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getTableName);
        
        columnList.forEach(column -> {
            
            //可链接的定制属性
            if (column.getLink() && column.getCustom()) {
                //取出该属性的id
                List<Object> ids = MyCollections.objects2List(jsonObjectList, (jsonObject -> jsonObject.getLong(column.getColumnName())));
                SQLFactors sqlFactors = new SQLFactors();
                sqlFactors.in("id", ids);
                List<JSONObject> select = mapper.select(realTableMap.get(column.getLinkTable()), sqlFactors.getSqlFactors());
                Map<Long, String> map = MyCollections.list2MapL(select, (k) -> k.getLong("id"), (v) -> v.getString("name"));
                jsonObjectList.forEach(object -> {
                    String columnName = column.getColumnName();
                    String columnNameR = column.getColumnName() + "R";
                    Long id = object.getLong(columnName);
                    //多条查询时覆盖原值
                    if (one) {
                        object.put(columnNameR, new IdToName(id, map.get(id), tableMap.get(column.getLinkTable())));
                    } else {
                        object.put(columnName, new IdToName(id, map.get(id), tableMap.get(column.getLinkTable())));
                    }
                });
            }
            
        });
        return jsonObjectList;
    }
}
