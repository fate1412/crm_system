package com.fate1412.crmSystem.module.customTable.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.module.customTable.dto.select.CustomTableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.CustomTableSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.module.customTable.service.ITableColumnDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.pojo.Customer;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import com.fate1412.crmSystem.utils.SQLFactor.SQLFactors;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private ISysFlowSessionService flowSessionService;
    
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
        
        //加入审批状态
        TableColumnDict pass = TableColumnDict.create(tableName,"pass","","是否通过",1000).setDisabled(true).setCustom(false).setColumnType(4);
        
        tableColumnDictList.add(pass);
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
            if (dto.getColumnName().equals("pass")) {
                dto.setPass(true);
            }
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
        String id = like.getString("id");
        String name = like.getString("name");
        if (StringUtils.isNotBlank(id)) {
            sqlFactors.like("id",id);
        }
        if (StringUtils.isNotBlank(name)) {
            sqlFactors.like("name",name.trim());
        }
        sqlFactors.eq("del_flag",false);
        
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
        List<JSONObject> dtoList = getDTOList(tableDict.getTableName(), jsonObjectList, tableResultData.getTableColumns(), true);
        tableResultData.setTableDataList(dtoList);
        return tableResultData;
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(JSONObject jsonObject) {
        //获取对应的表
        String tableName = jsonObject.getString("tableName").trim();
        if (StringUtils.isBlank(tableName)) {
            throw new DataCheckingException(ResultCode.INSERT_ERROR);
        }
        TableDict tableDict = tableDictService.getCustomByTableName(tableName);
        if (tableDict == null) {
            throw new DataCheckingException(ResultCode.INSERT_ERROR);
        }
        //查询表字段
        List<TableColumnDict> columnDictList = tableColumnDictService.listByTableName(tableDict.getTableName());
        //获取当前用户
        SysUser thisUser = sysUserService.thisUser();
        SQLFactors sqlFactors = new SQLFactors();
        //设置在新增时可填写的数据
        columnDictList.forEach(column -> {
            if (column.getInserted()) {
                //设置字段名和值，用于新增
                sqlFactors.eq(column.getRealColumnName(),getColumnData(column,jsonObject));
            }
        });
        //设置必备字段数据
        Long id = IdWorker.getId();
        sqlFactors.eq("id",id);
        sqlFactors.eq("create_time",new Date());
        sqlFactors.eq("update_time",new Date());
        sqlFactors.eq("creater",thisUser.getUserId());
        sqlFactors.eq("updater",thisUser.getUserId());
        
        //插入
        if (mapper.insertList(tableDict.getRealTableName(),MyCollections.toList(sqlFactors.getSqlFactors())) > 0) {
            flowSessionService.addFlowSession(tableDict.getTableName(), id);
            return ResultTool.success();
        } else {
            throw new DataCheckingException(ResultCode.INSERT_ERROR);
        }
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateDTO(JSONObject jsonObject) {
        //获取对应的表
        String tableName = jsonObject.getString("tableName").trim();
        if (StringUtils.isBlank(tableName)) {
            throw new DataCheckingException(ResultCode.UPDATE_ERROR);
        }
        TableDict tableDict = tableDictService.getCustomByTableName(tableName);
        if (tableDict == null) {
            throw new DataCheckingException(ResultCode.UPDATE_ERROR);
        }
        //查询表字段
        List<TableColumnDict> columnDictList = tableColumnDictService.listByTableName(tableDict.getTableName());
        //获取当前用户
        SysUser thisUser = sysUserService.thisUser();
        SQLFactors sqlFactors = new SQLFactors();
        //获取当前数据
        SQLFactors dataFactors = new SQLFactors();
        dataFactors
                .eq("id",jsonObject.getLong("id"))
                .eq("del_flag",false);
        List<JSONObject> jsonObjectList = mapper.select(tableDict.getRealTableName(), dataFactors.getSqlFactors());
        if (MyCollections.isEmpty(jsonObjectList)) {
            throw new DataCheckingException(ResultCode.COMMON_FAIL);
        }
        JSONObject object = jsonObjectList.get(0);
        //设置在编辑时可修改的数据
        columnDictList.forEach(column -> {
            if (!column.getDisabled()) {
                //设置字段名和值，用于编辑
                sqlFactors.eq(column.getRealColumnName(),getColumnData(column,jsonObject));
            } else {
                if (column.getColumnName().equals("updateTime")) {
                    sqlFactors.eq("update_time",new Date());
                } else if (column.getColumnName().equals("updater")) {
                    sqlFactors.eq("updater",thisUser.getUserId());
                } else {
                    //不更新的属性
                    sqlFactors.eq(column.getRealColumnName(), object.get(column.getRealColumnName()));
                }
            }
        });
    
        //更新
        if (mapper.updateList(tableDict.getRealTableName(),MyCollections.toList(sqlFactors.getSqlFactors())) > 0) {
            return ResultTool.success();
        } else {
            throw new DataCheckingException(ResultCode.UPDATE_ERROR);
        }
    }
    
    @Override
    public boolean delById(String tableName, Long id) {
        TableDict tableDict = tableDictService.getCustomByTableName(tableName.trim());
        if (tableDict == null) {
            return false;
        }
        SQLFactors sqlFactors = new SQLFactors();
        sqlFactors.eq("id",id);
        return mapper.deleteList(tableDict.getRealTableName(), sqlFactors.getSqlFactors()) > 0;
    }
    
    @Override
    public List<IdToName> getOptions(String tableName, String nameLike, Integer page) {
        TableDict tableDict = tableDictService.getCustomByTableName(tableName.trim());
        if (tableDict == null) {
            return new ArrayList<>();
        }
        SQLFactors sqlFactors = new SQLFactors();
        sqlFactors
                .like("name",nameLike.trim())
                .eq("del_flag",false);
        PageHelper.startPage(page,10);
        List<JSONObject> jsonObjectList = mapper.select(tableDict.getRealTableName(), sqlFactors.getSqlFactors());
        return IdToName.createList(jsonObjectList,k -> k.getLong("id"), v -> v.getString("name"));
    }
    
    private MyPage listByPage(int thisPage, int pageSize, String tableName, List<SQLFactor<Object>> sqlFactors, List<CustomTableColumnSelectDTO> columnList) {
        MyPage myPage = new MyPage(thisPage, pageSize);
        //获取/判断是否为定制表
        List<TableDict> tableDictList = tableDictService.getByTableName(MyCollections.toList(tableName.trim()));
        if (MyCollections.isEmpty(tableDictList) || !tableDictList.get(0).getCustom()) {
            return myPage;
        }
        
        PageInfo<JSONObject> pageInfo = PageHelper.startPage(thisPage, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                mapper.select(tableName.trim(), sqlFactors);
            }
        });
        
        List<JSONObject> dtoList = getDTOList(tableName.trim(), pageInfo.getList(), columnList, false);
        
        myPage.setRecords(dtoList);
        myPage.setCurrent(pageInfo.getPageNum());
        myPage.setTotal(pageInfo.getTotal());
        return myPage;
    }
    
    private List<JSONObject> getDTOList(String tableName, List<JSONObject> jsonList, List<CustomTableColumnSelectDTO> columnList, boolean one) {
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
    
        //审批
        List<Long> dataIds = MyCollections.objects2List(jsonObjectList, (jsonObject -> jsonObject.getLong("id")));
        Map<Long, Integer> passMap = flowSessionService.getPass(tableName.trim(), dataIds);
        
        jsonObjectList.forEach(object -> {
            Long creater = object.getLong("creater");
            Long updater = object.getLong("updater");
            Long id = object.getLong("id");
            //多条查询时覆盖原值
            if (one) {
                object.put("createrR", new IdToName(creater, userMap.get(creater), TableNames.sysUser));
                object.put("updaterR", new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            } else {
                object.put("creater", new IdToName(creater, userMap.get(creater), TableNames.sysUser));
                object.put("updater", new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            }
            Integer pass = passMap.get(id);
            switch (pass) {
                case 0: object.put("pass","未审批");break;
                case 1: object.put("pass","已通过");break;
                default: object.put("pass","已拒绝");
            }
        });
        //获取可链接的相关表
        List<Long> tableIds = MyCollections.objects2List(columnList, CustomTableColumnSelectDTO::getLinkTable);
        List<TableDict> tableDictList = mapper.selectBatchIds(tableIds);
        Map<Long, String> realTableMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getRealTableName);
        Map<Long, String> tableMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getTableName);
        
        columnList.forEach(column -> {
            //时间格式化
            if(column.getColumnType().equals(FormType.DateTime.getIndex()) || column.getColumnType().equals(FormType.Date.getIndex())) {
                //Date类型默认使用yyyy-MM-dd hh:mm:ss格式
                jsonObjectList.forEach(object -> {
                    object.put(column.getColumnName(),object.getDate(column.getColumnName()));
                });
            }
            
            //可链接的定制属性
            if (column.getLink() && column.getCustom()) {
                //取出该属性的id
                List<Object> ids = MyCollections.objects2List(jsonObjectList, (jsonObject -> jsonObject.getLong(column.getColumnName())));
                SQLFactors sqlFactors = new SQLFactors();
                sqlFactors.in("id", ids);
                List<JSONObject> select = mapper.select(realTableMap.get(column.getLinkTable()), sqlFactors.getSqlFactors());
                Map<Long, String> map = MyCollections.list2MapL(select, (k) -> k.getLong("id"),
                        (v) -> v.getString("name") == null? v.getString("id") : v.getString("name"));
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
    
    private Object getColumnData(TableColumnDict column, JSONObject jsonObject) {
        Integer columnType = column.getColumnType();
        try {
            if (columnType.equals(FormType.Select.getIndex())) {
                return jsonObject.getString(column.getColumnName().trim());
            } else if (columnType.equals(FormType.Date.getIndex()) || columnType.equals(FormType.DateTime.getIndex())) {
                return jsonObject.getDate(column.getColumnName());
            } else if (columnType.equals(FormType.Boolean.getIndex())) {
                return jsonObject.getBoolean(column.getColumnName());
            } else if (columnType.equals(FormType.String.getIndex())) {
                return jsonObject.getString(column.getColumnName().trim());
            } else if (columnType.equals(FormType.Integer.getIndex())) {
                return jsonObject.getInteger(column.getColumnName());
            } else if (columnType.equals(FormType.Double.getIndex())) {
                return jsonObject.getDouble(column.getColumnName());
            } else if (columnType.equals(FormType.Number.getIndex())) {
                return jsonObject.getString(column.getColumnName().trim());
            } else if (columnType.equals(FormType.Long.getIndex())) {
                return jsonObject.getLong(column.getColumnName());
            } else {
                throw new DataCheckingException(ResultCode.PARAM_TYPE_ERROR);
            }
        } catch (Exception e) {
            throw new DataCheckingException(ResultCode.PARAM_NOT_VALID);
        }
    }
}
