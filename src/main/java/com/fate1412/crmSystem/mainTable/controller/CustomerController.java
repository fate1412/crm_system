package com.fate1412.crmSystem.mainTable.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.CustomerSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.CustomerUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.ICustomerService;
import com.fate1412.crmSystem.utils.*;
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
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        CustomerSelectDTO customerSelectDTO = new CustomerSelectDTO();
        TableResultData tableResultData = TableResultData.createTableResultData(MyCollections.toList(customerSelectDTO), CustomerSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
//        IPage<CustomerSelectDTO> page = customerService.listByPage(thisPage, pageSize);
        MyPage page = customerService.listByPage(thisPage, pageSize);
        List<?> records = page.getRecords();
        TableResultData tableResultData = TableResultData.createTableResultData(records, CustomerSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody CustomerSelectDTO customerSelectDTO) {
        return customerService.add(customerSelectDTO);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> customerSelectDTOList = customerService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = TableResultData.createTableResultData(customerSelectDTOList, CustomerSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return customerService.updateById(customerUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody CustomerSelectDTO customerSelectDTO) {
        boolean b = customerService.removeById(customerSelectDTO.getId());
        return ResultTool.create(b);
    }
}

