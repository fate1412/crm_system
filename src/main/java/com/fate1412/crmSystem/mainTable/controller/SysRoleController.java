package com.fate1412.crmSystem.mainTable.controller;

import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.security.dto.insert.SysRoleInsertDTO;
import com.fate1412.crmSystem.security.dto.select.SysUserSelectDTO;
import com.fate1412.crmSystem.security.dto.update.SysRoleUpdateDTO;
import com.fate1412.crmSystem.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.security.service.ISysRoleService;
import com.fate1412.crmSystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sysRole")
@Slf4j
public class SysRoleController {
    @Autowired
    private ISysRoleService sysRoleService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = sysRoleService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = sysRoleService.getColumns();
        tableResultData.setTableDataList(dtoListById);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = sysRoleService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 10 : pageSize;
//        IPage<CustomerSelectDTO> page = customerService.listByPage(thisPage, pageSize);
        MyPage page = sysRoleService.listByPage(thisPage, pageSize,null);
        List<?> records = page.getRecords();
        TableResultData tableResultData = sysRoleService.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysRoleInsertDTO sysRoleInsertDTO) {
        return sysRoleService.addByDTO(sysRoleInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysRoleUpdateDTO sysRoleUpdateDTO) {
        return sysRoleService.updateByDTO(sysRoleUpdateDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysUserSelectDTO sysUserSelectDTO) {
        boolean b = sysRoleService.removeById(sysUserSelectDTO.getUserId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = sysRoleService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
    

}