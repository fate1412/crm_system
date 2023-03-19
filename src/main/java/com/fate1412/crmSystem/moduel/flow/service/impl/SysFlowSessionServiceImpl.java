package com.fate1412.crmSystem.moduel.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.moduel.customTable.pojo.TableDict;
import com.fate1412.crmSystem.moduel.customTable.service.ITableDictService;
import com.fate1412.crmSystem.moduel.flow.mapper.SysFlowMapper;
import com.fate1412.crmSystem.moduel.flow.mapper.SysFlowPointMapper;
import com.fate1412.crmSystem.moduel.flow.pojo.SysFlow;
import com.fate1412.crmSystem.moduel.flow.pojo.SysFlowPoint;
import com.fate1412.crmSystem.moduel.flow.pojo.SysFlowSession;
import com.fate1412.crmSystem.moduel.flow.mapper.SysFlowSessionMapper;
import com.fate1412.crmSystem.moduel.flow.service.ISysFlowSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
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
    private SysFlowMapper flowMapper;
    @Autowired
    private SysFlowPointMapper flowPointMapper;
    @Autowired
    private ITableDictService tableDictService;
    
    @Override
    public List<SysFlowSession> getSysFlowSession(String tableName, Long dataId) {
        QueryWrapper<SysFlowSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysFlowSession::getTableName, tableName)
                .eq(SysFlowSession::getDataId,dataId);
        return mapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean addFlowSession(String tableName, Long dataId) {
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
                .eq(SysFlowPoint::getFlowId,flow.getId())
                .orderByAsc(SysFlowPoint::getPanelPoint);
        List<SysFlowPoint> flowPointList = flowPointMapper.selectList(pointQueryWrapper);
        if (MyCollections.isEmpty(flowPointList)) {
            return false;
        }
        Map<Long, Long> nextMap = MyCollections.list2MapL(flowPointList, SysFlowPoint::getId, SysFlowPoint::getNextPoint);
    
        List<SysFlowSession> sysFlowSessionList = getSysFlowSession(tableName, dataId);
        SysFlowSession sysFlowSession = new SysFlowSession();
        sysFlowSession
                .setFlowId(flow.getId())
                .setDataId(dataId)
                .setTableName(tableName);
        if (MyCollections.isEmpty(sysFlowSessionList)) {
            //新审批
            sysFlowSession.setPointId(flowPointList.get(0).getId());
        } else {
            //获取当前节点
            SysFlowSession flowSession = sysFlowSessionList.get(sysFlowSessionList.size() - 1);
            sysFlowSession.setPointId(nextMap.get(flowSession.getPointId()));
        }
        
        return mapper.insert(sysFlowSession) > 0;
    }
}
