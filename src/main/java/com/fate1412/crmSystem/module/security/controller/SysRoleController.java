package com.fate1412.crmSystem.module.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.security.dto.insert.SysRoleInsertDTO;
import com.fate1412.crmSystem.module.security.dto.select.SysRolePermissionDTO;
import com.fate1412.crmSystem.module.security.dto.select.SysRoleSelectDTO;
import com.fate1412.crmSystem.module.security.dto.update.SysRoleUpdateDTO;
import com.fate1412.crmSystem.module.security.service.ISysRoleService;
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
    
    @PreAuthorize("hasAnyAuthority('SysRole_Select')")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = sysRoleService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = sysRoleService.getColumns();
        tableResultData.setTableDataList(dtoListById);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Select')")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = sysRoleService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Select')")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<SysRoleSelectDTO> selectPage) {
        MyPage page = sysRoleService.listByPage(selectPage);
        List<?> records = page.getRecords();
        TableResultData tableResultData = sysRoleService.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysRoleInsertDTO sysRoleInsertDTO) {
        return sysRoleService.addByDTO(sysRoleInsertDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysRoleUpdateDTO sysRoleUpdateDTO) {
        return sysRoleService.updateByDTO(sysRoleUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysRoleSelectDTO sysRoleSelectDTO) {
        boolean b = sysRoleService.removeRole(sysRoleSelectDTO.getRoleId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Select')")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = sysRoleService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
    
    @PreAuthorize("hasAnyAuthority('SysPermission_Select')")
    @GetMapping("/getPermissions")
    public JsonResult<?> getPermissions(@Param("id") Long id) {
        List<SysRolePermissionDTO> rolePermissionDTOList = sysRoleService.getRolePermissionDTOById(id);
        return ResultTool.success(rolePermissionDTOList);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRolePermission_Edit')")
    @PostMapping("/updatePermissions")
    public JsonResult<?> updatePermissions(@RequestBody JSONObject jsonObject) {
        Long id = jsonObject.getLong("id");
        List<SysRolePermissionDTO> list = jsonObject.getJSONArray("RolePermissionsList").toJavaList(SysRolePermissionDTO.class);
        boolean b = sysRoleService.updatePermissions(id, list);
        return b?ResultTool.success():ResultTool.fail();
    }
    
    @PreAuthorize("hasAnyAuthority('SysPermission_Select')")
    @GetMapping("/getPermissionsOptions")
    public JsonResult<?> getPermissionsOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = sysRoleService.getPermissionsOptions(nameLike, page);
        return ResultTool.success(options);
    }

}
