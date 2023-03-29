package com.fate1412.crmSystem.module.customTable.controller;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.service.ICustomTableService;
import com.fate1412.crmSystem.module.mainTable.dto.child.InvoiceChild;
import com.fate1412.crmSystem.module.mainTable.dto.insert.InvoiceInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.InvoiceUpdateDTO;
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
    
    @PreAuthorize("hasAnyAuthority('Invoice_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody JSONObject jsonObject) {
        return service.addDTO(jsonObject);
    }

    @PreAuthorize("hasAnyAuthority('Invoice_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody JSONObject jsonObject) {
        return service.updateDTO(jsonObject);
    }

    @PreAuthorize("hasAnyAuthority('Invoice_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody JSONObject jsonObject) {
        String tableName = jsonObject.getString("tableName");
        Long id = jsonObject.getLong("id");
        boolean b = service.delById(tableName, id);
        return ResultTool.create(b);
    }
//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/getOptions")
//    public JsonResult<?> getOptions(@Param("nameLike") String tableName, @Param("nameLike") String nameLike, @Param("page") Integer page) {
//        List<IdToName> options = service.getOptions(tableName, nameLike, page);
//        return ResultTool.success(options);
//    }
}
