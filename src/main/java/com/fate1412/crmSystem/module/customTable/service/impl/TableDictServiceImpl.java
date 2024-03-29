package com.fate1412.crmSystem.module.customTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableDictInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.mapper.TableColumnDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowMapper;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowPointMapper;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowSessionMapper;
import com.fate1412.crmSystem.module.flow.pojo.SysFlow;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowPoint;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowSession;
import com.fate1412.crmSystem.module.flow.service.ISysFlowService;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private TableColumnDictMapper tableColumnDictMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private SysFlowMapper flowMapper;
    @Autowired
    private SysFlowSessionMapper flowSessionMapper;
    @Autowired
    private SysFlowPointMapper flowPointMapper;
    
    
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
    public TableDict getCustomByTableName(String tableName) {
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TableDict::getTableName, tableName)
                .eq(TableDict::getCustom,true);
        return mapper.selectOne(queryWrapper);
    }
    
    @Override
    public TableDict getByRealName(String realNames) {
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(TableDict::getRealTableName, realNames);
        return getOne(queryWrapper);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(TableDictInsertDTO tableDictInsertDTO) {
        TableDict tableDict = new TableDict();
        BeanUtils.copyProperties(tableDictInsertDTO, tableDict);
        return add(new MyEntity<TableDict>(tableDict) {
            @Override
            public ResultCode verification(TableDict tableDict) {
                return isRight(tableDict);
            }
            
            @Override
            public boolean after(TableDict tableDict) {
                return afterAdd(tableDict);
            }
        });
    }
    
    @Override
    @Transactional
    public boolean delById(Long id) {
        TableDict tableDict = mapper.selectById(id);
        //查询该表的审批流程
        QueryWrapper<SysFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysFlow::getRelevanceTable, id);
        SysFlow sysFlow = flowMapper.selectOne(queryWrapper);
        //删除该表相关所有流程
        if (sysFlow != null) {
            //查询该表下所有流程
            QueryWrapper<SysFlowSession> sessionWrapper = new QueryWrapper<>();
            sessionWrapper.lambda().eq(SysFlowSession::getFlowId, sysFlow.getId());
            List<SysFlowSession> list = flowSessionMapper.selectList(sessionWrapper);
            //有流程
            if (!MyCollections.isEmpty(list)) {
                //删除所有流程
                flowSessionMapper.delete(sessionWrapper);
            }
            //删除该流程下的所有流程节点
            QueryWrapper<SysFlowPoint> pointWrapper = new QueryWrapper<>();
            pointWrapper.lambda().eq(SysFlowPoint::getFlowId, sysFlow.getId());
            flowPointMapper.delete(pointWrapper);
            //删除审批流程
            flowMapper.delete(queryWrapper);
        }
        if (removeById(id)) {
            QueryWrapper<TableColumnDict> columnDictQueryWrapper = new QueryWrapper<>();
            columnDictQueryWrapper.lambda().eq(TableColumnDict::getTableName, tableDict.getTableName());
            if (tableColumnDictMapper.delete(columnDictQueryWrapper) <= 0) {
                throw new DataCheckingException(ResultCode.COMMON_FAIL);
            }
            tableOptionService.delAllOption(tableDict.getTableName());
            mapper.delTable(tableDict.getRealTableName());
            return true;
        }
        return false;
    }
    
    @Override
    public MyPage listByPage(SelectPage<TableDictSelectDTO> selectPage) {
        TableDictSelectDTO like = selectPage.getLike();
        QueryWrapper<TableDict> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(like.getShowName())) {
            like.setShowName(like.getShowName().trim());
        }
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(like.getShowName()), TableDict::getShowName, like.getShowName());
        return listByPage(1, 1000, queryWrapper);
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
        return getColumns(TableNames.tableDict, new TableDictSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<?> getDTOList(List<TableDict> tableDictList) {
        return MyCollections.copyListProperties(tableDictList, TableDictSelectDTO::new);
    }
    
    @Override
    public BaseMapper<TableDict> mapper() {
        return mapper;
    }
    
    private ResultCode isRight(TableDict tableDict) {
        //真实表名
        if (StringUtils.isBlank(tableDict.getRealTableName())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        tableDict.setRealTableName(tableDict.getRealTableName().trim());
        if (tableDict.getRealTableName().matches(".*[^a-zA-Z0-9_].*")) {
            return ResultCode.PARAM_NOT_VALID;
        }
        TableDict byRealName = getByRealName(tableDict.getRealTableName());
        if (byRealName != null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //表名
        if (StringUtils.isBlank(tableDict.getTableName())) {
            return ResultCode.PARAM_NOT_VALID;
        }
        tableDict.setTableName(tableDict.getTableName().trim());
        List<TableDict> byTableName = getByTableName(MyCollections.toList(tableDict.getTableName()));
        if (!MyCollections.isEmpty(byTableName)) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //展示名
        if (StringUtils.isBlank(tableDict.getShowName())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        tableDict.setShowName(tableDict.getShowName().trim());
        return ResultCode.SUCCESS;
    }
    
    private boolean afterAdd(TableDict tableDict) {
        String tableName = tableDict.getTableName();
        String showName = tableDict.getShowName();
        List<TableColumnDict> list = new ArrayList<>();
        list.add(TableColumnDict.create(tableName, "id", "id", showName + "Id", 1)
                .setColumnType(9)
                .setDisabled(true)
                .setInserted(false)
                .setLink(true)
                .setCustom(false));
        list.add(TableColumnDict.create(tableName, "name", "name", "name", 2)
                .setColumnType(5)
                .setDisabled(true)
                .setInserted(true)
                .setCustom(false));
        list.add(TableColumnDict.create(tableName, "createTime", "create_time", "创建时间", 21)
                .setColumnType(3)
                .setDisabled(true)
                .setInserted(false)
                .setCustom(false));
        list.add(TableColumnDict.create(tableName, "updateTime", "update_time", "更新时间", 22)
                .setColumnType(3)
                .setDisabled(true)
                .setInserted(false)
                .setCustom(false));
        list.add(TableColumnDict.create(tableName, "creater", "creater", "创建人", 23)
                .setColumnType(1)
                .setDisabled(true)
                .setInserted(false)
                .setLink(true)
                .setCustom(false)
                .setLinkTable(9L));
        list.add(TableColumnDict.create(tableName, "updater", "updater", "修改人", 24)
                .setColumnType(1)
                .setDisabled(true)
                .setInserted(false)
                .setLink(true)
                .setCustom(false)
                .setLinkTable(9L));
        boolean b= tableColumnDictMapper.insertList(list) > 0;
        if (!b) {
            throw new DataCheckingException(ResultCode.COMMON_FAIL);
        }
        mapper.createTable(tableDict);
        return true;
    }
}
