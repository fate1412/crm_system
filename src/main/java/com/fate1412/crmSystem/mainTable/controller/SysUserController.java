package com.fate1412.crmSystem.mainTable.controller;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.security.dto.insert.SysUserInsertDTO;
import com.fate1412.crmSystem.security.dto.select.SysUserRolesDTO;
import com.fate1412.crmSystem.security.dto.select.SysUserSelectDTO;
import com.fate1412.crmSystem.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.security.pojo.SysRole;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController {
    @Autowired
    private ISysUserService sysUserService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = sysUserService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = sysUserService.getColumns();
        tableResultData.setTableDataList(dtoListById);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = sysUserService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 10 : pageSize;
        MyPage page = sysUserService.listByPage(thisPage, pageSize,null);
        List<?> records = page.getRecords();
        TableResultData tableResultData = sysUserService.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SysUser_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysUserInsertDTO sysUserInsertDTO) {
        return sysUserService.addByDTO(sysUserInsertDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SysUser_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysUserUpdateDTO sysUserUpdateDTO) {
        return sysUserService.updateByDTO(sysUserUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SysUser_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysUserSelectDTO sysUserSelectDTO) {
        boolean b = sysUserService.removeUser(sysUserSelectDTO.getUserId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = sysUserService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
    
    @PreAuthorize("hasAnyAuthority('SysRole_Select')")
    @GetMapping("/getRoles")
    public JsonResult<?> getRoles(@Param("id") Long id) {
        List<SysUserRolesDTO> userRolesList = sysUserService.getUserRolesById(id);
        return ResultTool.success(userRolesList);
    }
    
    @PreAuthorize("hasAnyAuthority('SysUserRole_Edit')")
    @PostMapping("/updateRoles")
    public JsonResult<?> updateRoles(@RequestBody JSONObject jsonObject) {
        Long id = jsonObject.getLong("id");
        List<SysUserRolesDTO> userRolesList = jsonObject.getJSONArray("userRolesList").toJavaList(SysUserRolesDTO.class);
        boolean b = sysUserService.updateRoles(id, userRolesList);
        return b?ResultTool.success():ResultTool.fail();
    }

}
