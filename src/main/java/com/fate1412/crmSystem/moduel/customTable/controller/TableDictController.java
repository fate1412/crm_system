package com.fate1412.crmSystem.moduel.customTable.controller;


import com.fate1412.crmSystem.moduel.customTable.service.ITableDictService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultTool;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 数据库表字典表 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/tableDict")
public class TableDictController {
    @Autowired
    private ITableDictService service;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = service.getOptions(nameLike, page);
        return ResultTool.success(options);
    }

}

