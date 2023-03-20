package com.fate1412.crmSystem.module.customTable.controller;

import com.fate1412.crmSystem.module.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultTool;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom")
public class CustomTableController {
    @Autowired
    private ICustomTableService service;
    
    @GetMapping("/tables")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<?> getTables() {
        return ResultTool.success(service.getTables());
    }
    
    @GetMapping("/tableColumns")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<?> getTableColumns(@Param("tableName") String tableName) {
        return ResultTool.success(service.getTableColumns(tableName));
    }
}
