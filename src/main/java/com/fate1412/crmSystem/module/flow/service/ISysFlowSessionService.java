package com.fate1412.crmSystem.module.flow.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowSessionSelectDTO;
import com.fate1412.crmSystem.module.flow.pojo.SysFlowSession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
public interface ISysFlowSessionService extends IService<SysFlowSession>, MyBaseService<SysFlowSession> {
    
    List<SysFlowSession> getSysFlowSession(String tableName, Long dataId);
    
    List<SysFlowSession> getSysFlowSession(String tableName, List<Long> dataIds);
    
    Map<Long,Integer> getPass(String tableName, List<Long> dataIds);
    
    boolean addFlowSession(String tableName, Long dataId, Long sessionId, Integer agree);
    
    boolean addFlowSession(String tableName, Long dataId);
    
    MyPage listByPage(SelectPage<SysFlowSessionSelectDTO> selectPage);
}
