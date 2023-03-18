package com.fate1412.crmSystem.moduel.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.moduel.flow.dto.insert.SysFlowInsertDTO;
import com.fate1412.crmSystem.moduel.flow.dto.select.SysFlowSelectDTO;
import com.fate1412.crmSystem.moduel.flow.dto.update.SysFlowUpdateDTO;
import com.fate1412.crmSystem.moduel.flow.pojo.SysFlow;
import com.fate1412.crmSystem.moduel.mainTable.dto.select.CustomerSelectDTO;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
public interface ISysFlowService extends IService<SysFlow>, MyBaseService<SysFlow> {
    JsonResult<?> updateByDTO(SysFlowUpdateDTO sysFlowUpdateDTO);
    
    JsonResult<?> addDTO(SysFlowInsertDTO sysFlowInsertDTO);
    
    JsonResult<?> addEntity(SysFlow sysFlow);
    
    JsonResult<?> updateByEntity(SysFlow sysFlow);
    
    MyPage listByPage(SelectPage<SysFlowSelectDTO> selectPage);
}
