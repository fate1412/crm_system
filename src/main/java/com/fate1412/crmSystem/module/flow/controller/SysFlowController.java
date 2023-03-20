package com.fate1412.crmSystem.module.flow.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.flow.dto.insert.SysFlowInsertDTO;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowPointSelectDTO;
import com.fate1412.crmSystem.module.flow.dto.select.SysFlowSelectDTO;
import com.fate1412.crmSystem.module.flow.dto.update.SysFlowUpdateDTO;
import com.fate1412.crmSystem.module.flow.service.ISysFlowService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/sysFlow")
public class SysFlowController {
    
    @Autowired
    private ISysFlowService service;
    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = service.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<SysFlowSelectDTO> selectPage) {
        MyPage page = service.listByPage(selectPage);
        List<?> records = page.getRecords();
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysFlowInsertDTO sysFlowInsertDTO) {
        return service.addDTO(sysFlowInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = service.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(dtoList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysFlowUpdateDTO sysFlowUpdateDTO) {
        return service.updateByDTO(sysFlowUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysFlowSelectDTO sysFlowSelectDTO) {
        boolean b = service.delFlow(sysFlowSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getFlowPoints")
    public JsonResult<Object> getFlowPoints(@Param("id") Long id) {
        List<SysFlowPointSelectDTO> flowPoints = service.getFlowPoints(id);
        return ResultTool.success(flowPoints);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateFlowPoints")
    public JsonResult<?> updateFlowPoints(@RequestBody JSONObject jsonObject) {
        Long flowId = jsonObject.getLong("flowId");
        JSONArray jsonArray = jsonObject.getJSONArray("flowPoints");
        List<SysFlowPointSelectDTO> flowPoints = jsonArray.toJavaList(SysFlowPointSelectDTO.class);
        boolean b = service.updateFlowPoints(flowId, flowPoints);
        return b? ResultTool.success(): ResultTool.fail();
    }
}

