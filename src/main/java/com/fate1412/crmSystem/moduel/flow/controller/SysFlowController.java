package com.fate1412.crmSystem.moduel.flow.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.moduel.flow.dto.insert.SysFlowInsertDTO;
import com.fate1412.crmSystem.moduel.flow.dto.select.SysFlowSelectDTO;
import com.fate1412.crmSystem.moduel.flow.dto.update.SysFlowUpdateDTO;
import com.fate1412.crmSystem.moduel.flow.service.ISysFlowService;
import com.fate1412.crmSystem.moduel.mainTable.dto.insert.CustomerInsertDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.select.CustomerSelectDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.update.CustomerUpdateDTO;
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
    private ISysFlowService sysFlowService;
    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = sysFlowService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<SysFlowSelectDTO> selectPage) {
        MyPage page = sysFlowService.listByPage(selectPage);
        List<?> records = page.getRecords();
        TableResultData tableResultData = sysFlowService.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysFlowInsertDTO sysFlowInsertDTO) {
        return sysFlowService.addDTO(sysFlowInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = sysFlowService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = sysFlowService.getColumns();
        tableResultData.setTableDataList(dtoList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysFlowUpdateDTO sysFlowUpdateDTO) {
        return sysFlowService.updateByDTO(sysFlowUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysFlowSelectDTO sysFlowSelectDTO) {
        boolean b = sysFlowService.removeById(sysFlowSelectDTO.getId());
        return ResultTool.create(b);
    }
}

