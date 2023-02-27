package com.fate1412.crmSystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.dto.CustomerDTO;
import com.fate1412.crmSystem.pojo.Customer;
import com.fate1412.crmSystem.service.ICustomerService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 客户 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("thisPage") long thisPage) {
        IPage<CustomerDTO> page = customerService.listByPage(thisPage, 10);
        TableResultData tableResultData = ResultTool.createTableResultData(page,CustomerDTO.class);
        return ResultTool.success(tableResultData);
    }
}

