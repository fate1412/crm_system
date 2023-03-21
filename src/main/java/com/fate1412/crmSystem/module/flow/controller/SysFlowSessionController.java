package com.fate1412.crmSystem.module.flow.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowSessionSelectDTO;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/sysFlowSession")
public class SysFlowSessionController {
    @Autowired
    private ISysFlowSessionService service;
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<SysFlowSessionSelectDTO> selectPage) {
        MyPage page = service.listByPage(selectPage);
        List<?> records = page.getRecords();
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PostMapping("/approve")
    public JsonResult<?> approve(@RequestBody SysFlowSessionSelectDTO sessionSelectDTO) {
        boolean b = service.addFlowSession(sessionSelectDTO.getTableName(), sessionSelectDTO.getDataId(), sessionSelectDTO.getId(), sessionSelectDTO.getPass());
        return b? ResultTool.success() : ResultTool.fail();
    }

}

