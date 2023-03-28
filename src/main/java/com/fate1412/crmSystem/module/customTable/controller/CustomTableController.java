package com.fate1412.crmSystem.module.customTable.controller;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/custom")
public class CustomTableController {
    @Autowired
    private ICustomTableService service;
    
    
    @GetMapping("/tables")
    @PreAuthorize("permitAll()")
    public JsonResult<?> getTables() {
        return ResultTool.success(service.getTables());
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tableColumns")
    public JsonResult<Object> getColumns(@Param("tableName") String tableName) {
        CustomTableResultData tableResultData = service.getTableColumns(tableName);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<JSONObject> selectPage) {
        CustomTableResultData customTableResultData = service.listByPage(selectPage);
        return ResultTool.success(customTableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id, @Param("tableName") String tableName) {
        CustomTableResultData tableResultData = service.getDTOListById(tableName, MyCollections.toList(id));
        return ResultTool.success(tableResultData);
    }
}
