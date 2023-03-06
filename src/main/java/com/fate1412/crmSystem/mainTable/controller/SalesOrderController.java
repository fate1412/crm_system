package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.ProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.dto.SalesOrderSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.ISalesOrderService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 销售订单 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/salesOrder")
public class SalesOrderController {
    @Autowired
    private ISalesOrderService salesOrderService;
    
    @PreAuthorize("permitAll()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = salesOrderService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 10 : pageSize;
        MyPage page = salesOrderService.listByPage(thisPage, pageSize);
        TableResultData tableResultData = salesOrderService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SalesOrderUpdateDTO salesOrderUpdateDTO) {
        return salesOrderService.add(salesOrderUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = salesOrderService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = TableResultData.createTableResultData(dtoListById, SalesOrderSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SalesOrderUpdateDTO salesOrderUpdateDTO) {
        return salesOrderService.updateById(salesOrderUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SalesOrderSelectDTO salesOrderSelectDTO) {
        boolean b = salesOrderService.removeById(salesOrderSelectDTO.getId());
        return ResultTool.create(b);
    }
}

