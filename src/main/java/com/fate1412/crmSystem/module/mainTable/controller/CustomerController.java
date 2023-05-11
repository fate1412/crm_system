package com.fate1412.crmSystem.module.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.mainTable.dto.insert.CustomerInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.CustomerSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.CustomerUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.service.ICustomerService;
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
    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = customerService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<CustomerSelectDTO> selectPage) {
        MyPage page = customerService.listByPage(selectPage);
        List<?> records = page.getRecords();
        TableResultData tableResultData = customerService.getColumns();
        tableResultData.setTableDataList(records);
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody CustomerInsertDTO customerInsertDTO) {
        return customerService.addDTO(customerInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> customerSelectDTOList = customerService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = customerService.getColumns();
        tableResultData.setTableDataList(customerSelectDTOList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return customerService.updateByDTO(customerUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('Customer_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody CustomerSelectDTO customerSelectDTO) {
        boolean b = customerService.delById(customerSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = customerService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

