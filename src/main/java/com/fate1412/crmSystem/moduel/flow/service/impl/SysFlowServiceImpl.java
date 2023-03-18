package com.fate1412.crmSystem.moduel.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.moduel.customTable.pojo.TableDict;
import com.fate1412.crmSystem.moduel.customTable.service.ITableDictService;
import com.fate1412.crmSystem.moduel.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.moduel.flow.dto.insert.SysFlowInsertDTO;
import com.fate1412.crmSystem.moduel.flow.dto.select.SysFlowSelectDTO;
import com.fate1412.crmSystem.moduel.flow.dto.update.SysFlowUpdateDTO;
import com.fate1412.crmSystem.moduel.flow.mapper.SysFlowMapper;
import com.fate1412.crmSystem.moduel.flow.pojo.SysFlow;
import com.fate1412.crmSystem.moduel.flow.service.ISysFlowService;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
import com.fate1412.crmSystem.moduel.security.pojo.SysUser;
import com.fate1412.crmSystem.moduel.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fate1412.crmSystem.moduel.mainTable.constant.TableNames.*;

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
    
    
    @Override
    public JsonResult<?> updateByDTO(SysFlowUpdateDTO sysFlowUpdateDTO) {
        SysFlow sysFlow = new SysFlow();
        BeanUtils.copyProperties(sysFlowUpdateDTO, sysFlow);
        return updateByEntity(sysFlow);
    }
    
    @Override
    public JsonResult<?> addDTO(SysFlowInsertDTO sysFlowInsertDTO) {
        SysFlow sysFlow = new SysFlow();
        BeanUtils.copyProperties(sysFlowInsertDTO, sysFlow);
        return addEntity(sysFlow);
    }
    
    @Override
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
        });
    }
    
    @Override
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
    public List<IdToName> getOptions(String nameLike, Integer page) {
        return new ArrayList<>();
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysFlow,new SysFlowSelectDTO(), tableOptionService);
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
        //数据库表字典
        List<Integer> tableIds = MyCollections.objects2List(sysFlowList, SysFlow::getRelevanceTable);
        List<TableDict> tableDictList = tableDictService.listByIds(tableIds);
        Map<Integer, String> dictMap = MyCollections.list2MapL(tableDictList, TableDict::getId, TableDict::getShowName);
    
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        List<SysFlowSelectDTO> sysFlowSelectDTOList = MyCollections.copyListProperties(sysFlowList, SysFlowSelectDTO::new);
        sysFlowSelectDTOList.forEach(flowSelectDTO -> {
            Long createId = flowSelectDTO.getCreater();
            Long updaterId = flowSelectDTO.getUpdater();
            Integer relevanceTable = flowSelectDTO.getRelevanceTable();
            flowSelectDTO.setCreaterR(new IdToName(createId, userMap.get(createId), sysUser));
            flowSelectDTO.setUpdaterR(new IdToName(updaterId, userMap.get(updaterId), sysUser));
            flowSelectDTO.setRelevanceTableR(new IdToName(relevanceTable.longValue(), dictMap.get(relevanceTable), tableDict));
        });
        return sysFlowSelectDTOList;
    }
    
    @Override
    public BaseMapper<SysFlow> mapper() {
        return mapper;
    }
}
