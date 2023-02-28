package com.fate1412.crmSystem.mainTable.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.mainTable.dto.CustomerDTO;
import com.fate1412.crmSystem.mainTable.service.ICustomerService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
        IPage<CustomerDTO> page = customerService.listByPage(thisPage, pageSize);
        TableResultData tableResultData = ResultTool.createTableResultData(page, CustomerDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<CustomerDTO> customerDTOList = customerService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = ResultTool.createTableResultData(customerDTOList, CustomerDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/update")
    public JsonResult<?> update(@RequestBody CustomerDTO customerDTO) {
        boolean b = customerService.updateById(customerDTO);
        return ResultTool.create(b);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody CustomerDTO customerDTO) {
        boolean b = customerService.removeById(customerDTO.getId());
        return ResultTool.create(b);
    }
}

