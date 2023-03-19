package com.fate1412.crmSystem.moduel.flow.service;

import com.fate1412.crmSystem.moduel.flow.pojo.SysFlowSession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
public interface ISysFlowSessionService extends IService<SysFlowSession> {
    
    List<SysFlowSession> getSysFlowSession(String tableName, Long dataId);
    
    boolean addFlowSession(String tableName, Long dataId);

}
