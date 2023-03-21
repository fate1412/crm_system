package com.fate1412.crmSystem.module.flow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowSessionSelectDTO;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowMapper;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowPointMapper;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowSessionMapper;
import com.fate1412.crmSystem.module.flow.pojo.SysFlow;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowPoint;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowSession;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultCode;
import com.fate1412.crmSystem.utils.SQLFactor.SQLFactors;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
@Service
public class SysFlowSessionServiceImpl extends ServiceImpl<SysFlowSessionMapper, SysFlowSession> implements ISysFlowSessionService {
    
    @Autowired
    private SysFlowSessionMapper mapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysFlowMapper flowMapper;
    @Autowired
    private SysFlowPointMapper flowPointMapper;
    @Autowired
    private ITableDictService tableDictService;
    @Autowired
    private TableDictMapper tableDictMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<SysFlowSession> getSysFlowSession(String tableName, Long dataId) {
        QueryWrapper<SysFlowSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysFlowSession::getTableName, tableName)
                .eq(SysFlowSession::getDataId, dataId);
        return mapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean addFlowSession(String tableName, Long dataId) {
        return addFlowSession(tableName, dataId, null, null);
    }
    
    @Override
    @Transactional
    public boolean addFlowSession(String tableName, Long dataId, Long sessionId, Integer agree) {
        SysFlowSession oldSession = null;
        if (sessionId != null) {
            //获取当前审批任务最后一个节点
            oldSession = mapper.selectById(sessionId);
            //已审批
            if (oldSession.getPass() != 0) {
                throw new DataCheckingException(ResultCode.APPROVE);
            }
            //最后一个节点
            if (oldSession.getNextApprover() == null) {
                oldSession.setPass(agree == 1 ? 1 : 2);
                return mapper.updateById(oldSession) > 0;
            }
        }
        //查询是否存在此表
        List<TableDict> tableDictList = tableDictService.getByTableName(MyCollections.toList(tableName));
        if (MyCollections.isEmpty(tableDictList)) {
            return false;
        }
        TableDict tableDict = tableDictList.get(0);
        //查询是否存在流程
        QueryWrapper<SysFlow> flowQueryWrapper = new QueryWrapper<>();
        flowQueryWrapper.lambda().eq(SysFlow::getRelevanceTable, tableDict.getId());
        SysFlow flow = flowMapper.selectOne(flowQueryWrapper);
        if (flow == null) {
            return false;
        }
        //查出审批节点
        QueryWrapper<SysFlowPoint> pointQueryWrapper = new QueryWrapper<>();
        pointQueryWrapper.lambda()
                .eq(SysFlowPoint::getFlowId, flow.getId())
                .orderByAsc(SysFlowPoint::getPanelPoint);
        List<SysFlowPoint> flowPointList = flowPointMapper.selectList(pointQueryWrapper);
        if (MyCollections.isEmpty(flowPointList)) {
            return false;
        }
        Map<Long, SysFlowPoint> pointMap = MyCollections.list2MapL(flowPointList, SysFlowPoint::getId);
        //获取需要审批的数据
        SQLFactors sqlFactors = new SQLFactors();
        sqlFactors.eq("id", dataId);
        List<JSONObject> jsonObjectList = tableDictMapper.select(tableDict.getRealTableName(), sqlFactors.getSqlFactors());
        if (MyCollections.isEmpty(jsonObjectList)) {
            //数据不存在
            return false;
        }
        JSONObject data = jsonObjectList.get(0);

//        List<SysFlowSession> sysFlowSessionList = getSysFlowSession(tableName, dataId);
        
        SysFlowSession sysFlowSession = new SysFlowSession();
        sysFlowSession
                .setFlowId(flow.getId())
                .setDataId(dataId)
                .setCreater(data.getLong("creater"))
                .setTableName(tableDict.getTableName());
        if (sessionId == null) {
            //新审批
            sysFlowSession.setPointId(flowPointList.get(0).getId());
            sysFlowSession.setApprover(flowPointList.get(0).getApprover());
            Long nextPoint = flowPointList.get(0).getNextPoint();
            //设置下一个审批人
            if (pointMap.get(nextPoint) != null) {
                sysFlowSession.setNextApprover(pointMap.get(nextPoint).getApprover());
            }
        } else {
            //设置新节点
            sysFlowSession.setPointId(pointMap.get(oldSession.getPointId()).getNextPoint());
            sysFlowSession.setApprover(pointMap.get(oldSession.getPointId()).getApprover());
            //获取新节点的下一个节点
            Long nextPoint = pointMap.get(sysFlowSession.getPointId()).getNextPoint();
            //设置下一个审批人
            if (pointMap.get(nextPoint) != null) {
                sysFlowSession.setNextApprover(pointMap.get(nextPoint).getApprover());
            }
            oldSession.setPass(agree == 1 ? 1 : 2);
            mapper.updateById(oldSession);
        }
        
        return mapper.insert(sysFlowSession) > 0;
    }
    
    @Override
    public MyPage listByPage(SelectPage<SysFlowSessionSelectDTO> selectPage) {
        SysFlowSessionSelectDTO like = selectPage.getLike();
        QueryWrapper<SysFlowSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getDataId() != null, SysFlowSession::getDataId, like.getDataId());
        return listByPage(selectPage.getPage(), selectPage.getPageSize(), queryWrapper);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        return new ArrayList<>();
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysFlowSession, new SysFlowSessionSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<?> getDTOList(List<SysFlowSession> sysFlowSessions) {
        if (MyCollections.isEmpty(sysFlowSessions)) {
            return new ArrayList<>();
        }
        //表
        List<String> tableNames = MyCollections.objects2List(sysFlowSessions, SysFlowSession::getTableName);
        List<TableDict> tableDictList = tableDictService.getByTableName(tableNames);
        Map<String, String> tableMap = MyCollections.list2MapL(tableDictList, TableDict::getTableName, TableDict::getShowName);
        //用户
        List<Long> createrIds = MyCollections.objects2List(sysFlowSessions, SysFlowSession::getCreater);
//        List<Long> approverIds = MyCollections.objects2List(sysFlowSessions, SysFlowSession::getApprover);
        List<Long> nextApproverIds = MyCollections.objects2List(sysFlowSessions, SysFlowSession::getNextApprover);
        List<Long> userIds = MyCollections.addList(createrIds, nextApproverIds);
        List<SysUser> sysUserList = sysUserService.listByIds(userIds);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        
        List<SysFlowSessionSelectDTO> dtoList = MyCollections.copyListProperties(sysFlowSessions, SysFlowSessionSelectDTO::new);
        dtoList.forEach(dto -> {
            Long creater = dto.getCreater();
            Long nextApprover = dto.getNextApprover();
            String tableName = dto.getTableName();
            dto.setCreaterR(new IdToName(creater, userMap.get(creater), TableNames.sysUser));
            dto.setNextApproverR(new IdToName(nextApprover, userMap.get(nextApprover), TableNames.sysUser));
            dto.setTableNameR(tableMap.get(tableName));
            dto.setPassR(dto.getPassString());
        });
        return dtoList;
    }
    
    @Override
    public BaseMapper<SysFlowSession> mapper() {
        return mapper;
    }
}
