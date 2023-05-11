package com.fate1412.crmSystem.module.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.module.customTable.pojo.TableDict;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.flow.dto.insert.SysFlowInsertDTO;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowPointSelectDTO;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowSelectDTO;
import com.fate1412.crmSystem.module.flow.dto.update.SysFlowUpdateDTO;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowMapper;
import com.fate1412.crmSystem.module.flow.mapper.SysFlowSessionMapper;
import com.fate1412.crmSystem.module.flow.pojo.SysFlow;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowPoint;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowSession;
import com.fate1412.crmSystem.module.flow.service.ISysFlowPointService;
import com.fate1412.crmSystem.module.flow.service.ISysFlowService;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.fate1412.crmSystem.module.mainTable.constant.TableNames.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
@Service
public class SysFlowServiceImpl extends ServiceImpl<SysFlowMapper, SysFlow> implements ISysFlowService {
    @Autowired
    private SysFlowMapper mapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private ITableDictService tableDictService;
    @Autowired
    private ISysFlowPointService sysFlowPointService;
    @Autowired
    private SysFlowSessionMapper flowSessionMapper;
    
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(SysFlowUpdateDTO sysFlowUpdateDTO) {
        SysFlow sysFlow = new SysFlow();
        BeanUtils.copyProperties(sysFlowUpdateDTO, sysFlow);
        return updateByEntity(sysFlow);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(SysFlowInsertDTO sysFlowInsertDTO) {
        SysFlow sysFlow = new SysFlow();
        BeanUtils.copyProperties(sysFlowInsertDTO, sysFlow);
        return addEntity(sysFlow);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(SysFlow sysFlow) {
        SysUser sysUser = sysUserService.thisUser();
        return add(new MyEntity<SysFlow>(sysFlow) {
            @Override
            public SysFlow set(SysFlow sysFlow) {
                sysFlow
                        .setCreater(sysUser.getUserId())
                        .setCreateTime(new Date())
                        .setUpdater(sysUser.getUserId())
                        .setUpdateTime(new Date());
                return sysFlow;
            }
    
            @Override
            public ResultCode verification(SysFlow sysFlow) {
                return isRight(sysFlow);
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByEntity(SysFlow sysFlow) {
        SysUser sysUser = sysUserService.thisUser();
        return update(new MyEntity<SysFlow>(sysFlow) {
            @Override
            public SysFlow set(SysFlow sysFlow) {
                sysFlow
                        .setUpdater(sysUser.getUserId())
                        .setUpdateTime(new Date());
                return sysFlow;
            }
    
            @Override
            public ResultCode verification(SysFlow sysFlow) {
                return isRight(sysFlow);
            }
        });
    }
    
    @Override
    public MyPage listByPage(SelectPage<SysFlowSelectDTO> selectPage) {
        SysFlowSelectDTO like = selectPage.getLike();
        QueryWrapper<SysFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null, SysFlow::getId, like.getId())
                .like(like.getName() != null, SysFlow::getName, like.getName());
        return listByPage(selectPage.getPage(), selectPage.getPageSize(), queryWrapper);
    }
    
    @Override
    public List<SysFlowPointSelectDTO> getFlowPoints(Long sysFlowId) {
        QueryWrapper<SysFlowPoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysFlowPoint::getFlowId, sysFlowId)
                .orderByAsc(SysFlowPoint::getPanelPoint);
        List<SysFlowPoint> sysFlowPointList = sysFlowPointService.list(queryWrapper);
        if (MyCollections.isEmpty(sysFlowPointList)) {
            return null;
        }
        
        List<Long> userIds = MyCollections.objects2List(sysFlowPointList, SysFlowPoint::getApprover);
        List<SysUser> sysUsers = sysUserService.listByIds(userIds);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUsers, SysUser::getUserId, SysUser::getRealName);
        
        List<SysFlowPointSelectDTO> pointDTOList = MyCollections.copyListProperties(sysFlowPointList, SysFlowPointSelectDTO::new);
        pointDTOList.forEach(point -> {
            Long approver = point.getApprover();
            point.setApproverR(userMap.get(approver));
        });
        return pointDTOList;
    }
    
    @Override
    public List<SysFlowPoint> getFlowPoints(String tableName) {
        //查询是否存在此表
        List<TableDict> tableDictList = tableDictService.getByTableName(MyCollections.toList(tableName));
        if (MyCollections.isEmpty(tableDictList)) {
            return new ArrayList<>();
        }
        TableDict tableDict = tableDictList.get(0);
        //查询是否存在流程
        QueryWrapper<SysFlow> flowQueryWrapper = new QueryWrapper<>();
        flowQueryWrapper.lambda().eq(SysFlow::getRelevanceTable, tableDict.getId());
        SysFlow flow = mapper.selectOne(flowQueryWrapper);
        if (flow == null) {
            return new ArrayList<>();
        }
        //查出审批节点
        QueryWrapper<SysFlowPoint> pointQueryWrapper = new QueryWrapper<>();
        pointQueryWrapper.lambda()
                .eq(SysFlowPoint::getFlowId, flow.getId())
                .orderByAsc(SysFlowPoint::getPanelPoint);
        List<SysFlowPoint> flowPointList = sysFlowPointService.list(pointQueryWrapper);
        if (MyCollections.isEmpty(flowPointList)) {
            return new ArrayList<>();
        }
        return flowPointList;
    }
    
    @Override
    @Transactional
    public boolean updateFlowPoints(Long flowId, List<SysFlowPointSelectDTO> flowPointDTOList) {
        
        //删除
        QueryWrapper<SysFlowPoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysFlowPoint::getFlowId, flowId);
        sysFlowPointService.remove(queryWrapper);
    
        
        List<Long> userIds = MyCollections.objects2List(flowPointDTOList, SysFlowPointSelectDTO::getApprover);
        if(MyCollections.isEmpty(userIds)) {
            return true;
        }
        //验证审批人是否存在
        List<SysUser> sysUsers = sysUserService.listByIds(userIds);
        if (sysUsers.size() != flowPointDTOList.size()) {
            throw new DataCheckingException(ResultCode.PARAM_NOT_VALID);
        }
    
        flowPointDTOList.sort(Comparator.comparing(SysFlowPointSelectDTO::getPanelPoint));
        Long nextPoint = null;
        for(int i=flowPointDTOList.size()-1; i >= 0; i--) {
            SysFlowPointSelectDTO point = flowPointDTOList.get(i);
            if (i == flowPointDTOList.size()-1) {
                point.setNextPoint(-1L);
            } else {
                point.setNextPoint(nextPoint);
            }
            SysFlowPoint sysFlowPoint = new SysFlowPoint();
            BeanUtils.copyProperties(point,sysFlowPoint);
            //插入
            sysFlowPointService.save(sysFlowPoint);
            nextPoint = sysFlowPoint.getId();
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean delFlow(Long flowId) {
        //查询该表下所有流程
        QueryWrapper<SysFlowSession> sessionWrapper = new QueryWrapper<>();
        sessionWrapper.lambda().eq(SysFlowSession::getFlowId, flowId);
        List<SysFlowSession> list = flowSessionMapper.selectList(sessionWrapper);
        //有流程
        if (!MyCollections.isEmpty(list)) {
            //删除所有流程
            flowSessionMapper.delete(sessionWrapper);
        }
        QueryWrapper<SysFlowPoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysFlowPoint::getFlowId, flowId);
        sysFlowPointService.remove(queryWrapper);
        return removeById(flowId);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        return new ArrayList<>();
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysFlow, new SysFlowSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<?> getDTOList(List<SysFlow> sysFlowList) {
        if (MyCollections.isEmpty(sysFlowList)) {
            return new ArrayList<>();
        }
        //用户
        List<Long> createIds = MyCollections.objects2List(sysFlowList, SysFlow::getCreater);
        List<Long> updaterIds = MyCollections.objects2List(sysFlowList, SysFlow::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updaterIds);
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        //关联表
        List<Long> tableIds = MyCollections.objects2List(sysFlowList, SysFlow::getRelevanceTable);
        List<TableDict> tableDictList = tableDictService.listByIds(tableIds);
        Map<Long, String> dictMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getShowName);
        
        List<SysFlowSelectDTO> sysFlowSelectDTOList = MyCollections.copyListProperties(sysFlowList, SysFlowSelectDTO::new);
        sysFlowSelectDTOList.forEach(flowSelectDTO -> {
            Long createId = flowSelectDTO.getCreater();
            Long updaterId = flowSelectDTO.getUpdater();
            Long relevanceTable = flowSelectDTO.getRelevanceTable();
            flowSelectDTO.setCreaterR(new IdToName(createId, userMap.get(createId), sysUser));
            flowSelectDTO.setUpdaterR(new IdToName(updaterId, userMap.get(updaterId), sysUser));
            flowSelectDTO.setRelevanceTableR(new IdToName(relevanceTable, dictMap.get(relevanceTable), tableDict));
        });
        return sysFlowSelectDTOList;
    }
    
    @Override
    public BaseMapper<SysFlow> mapper() {
        return mapper;
    }
    
    private ResultCode isRight(SysFlow sysFlow) {
        //流程名称
        if (StringUtils.isBlank(sysFlow.getName())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        sysFlow.setName(sysFlow.getName().trim());
        //关联表
        if (sysFlow.getRelevanceTable() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        TableDict tableDict = tableDictService.getById(sysFlow.getRelevanceTable());
        if (tableDict == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        if (sysFlow.getId() == null) {
            QueryWrapper<SysFlow> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysFlow::getRelevanceTable, sysFlow.getRelevanceTable());
            SysFlow flow = getOne(queryWrapper);
            if (flow != null) {
                return ResultCode.PARAM_REPEAT;
            }
        }
    
        return ResultCode.SUCCESS;
    }
}
