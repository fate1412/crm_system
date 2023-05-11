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
import com.fate1412.crmSystem.utils.*;
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
    
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Select')")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = service.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Select')")
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
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysFlowInsertDTO sysFlowInsertDTO) {
        return service.addDTO(sysFlowInsertDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Select')")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = service.getDTOListById(MyCollections.toList(id));
        if (MyCollections.isEmpty(dtoList)) {
            return ResultTool.fail(ResultCode.DATA_NOT_FOUND);
        }
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(dtoList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysFlowUpdateDTO sysFlowUpdateDTO) {
        return service.updateByDTO(sysFlowUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysFlowSelectDTO sysFlowSelectDTO) {
        boolean b = service.delFlow(sysFlowSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Select')")
    @GetMapping("/getFlowPoints")
    public JsonResult<Object> getFlowPoints(@Param("id") Long id) {
        List<SysFlowPointSelectDTO> flowPoints = service.getFlowPoints(id);
        return ResultTool.success(flowPoints);
    }
    
    @PreAuthorize("hasAnyAuthority('SysFlow_Edit')")
    @PostMapping("/updateFlowPoints")
    public JsonResult<?> updateFlowPoints(@RequestBody JSONObject jsonObject) {
        Long flowId = jsonObject.getLong("flowId");
        JSONArray jsonArray = jsonObject.getJSONArray("flowPoints");
        List<SysFlowPointSelectDTO> flowPoints = jsonArray.toJavaList(SysFlowPointSelectDTO.class);
        boolean b = service.updateFlowPoints(flowId, flowPoints);
        return b? ResultTool.success(): ResultTool.fail();
    }
}

