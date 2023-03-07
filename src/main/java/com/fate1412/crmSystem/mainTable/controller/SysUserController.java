package com.fate1412.crmSystem.mainTable.controller;

import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.security.dto.SysUserSelectDTO;
import com.fate1412.crmSystem.security.dto.SysUserUpdateDTO;
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
//        IPage<CustomerSelectDTO> page = customerService.listByPage(thisPage, pageSize);
        MyPage page = sysUserService.listByPage(thisPage, pageSize,null);
        List<?> records = page.getRecords();
        TableResultData tableResultData = sysUserService.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SysUserUpdateDTO sysUserUpdateDTO) {
        return sysUserService.add(sysUserUpdateDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SysUserUpdateDTO sysUserUpdateDTO) {
        return sysUserService.updateById(sysUserUpdateDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SysUserSelectDTO sysUserSelectDTO) {
        boolean b = sysUserService.removeById(sysUserSelectDTO.getUserId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = sysUserService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}
