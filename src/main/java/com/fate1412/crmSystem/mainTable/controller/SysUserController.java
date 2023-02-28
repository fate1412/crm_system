package com.fate1412.crmSystem.mainTable.controller;

import com.fate1412.crmSystem.mainTable.dto.CustomerDTO;
import com.fate1412.crmSystem.security.dto.SysUserDTO;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
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
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<SysUserDTO> sysUserDTOList = sysUserService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = ResultTool.createTableResultData(sysUserDTOList, SysUserDTO.class);
        return ResultTool.success(tableResultData);
    }
}
