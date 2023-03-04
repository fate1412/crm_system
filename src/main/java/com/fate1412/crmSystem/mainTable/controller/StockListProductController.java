package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.*;
import com.fate1412.crmSystem.mainTable.service.IStockListProductService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 备货单产品 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/stockListProduct")
public class StockListProductController {
    @Autowired
    private IStockListProductService stockListProductService;
    
    @PreAuthorize("permitAll()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        List<TableColumn> tableColumns = TableResultData.tableColumnList(StockListProductSelectDTO.class);
        return ResultTool.success(tableColumns);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
        MyPage page = stockListProductService.listByPage(thisPage, pageSize);
        TableResultData tableResultData = TableResultData.createTableResultData(page.getRecords(), StockListProductSelectDTO.class,thisPage,page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody StockListProductUpdateDTO stockListProductUpdateDTO) {
        return stockListProductService.add(stockListProductUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = stockListProductService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = TableResultData.createTableResultData(dtoListById, StockListProductSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody StockListProductUpdateDTO stockListProductUpdateDTO) {
        return stockListProductService.updateById(stockListProductUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody StockListProductSelectDTO stockListProductSelectDTO) {
        boolean b = stockListProductService.removeById(stockListProductSelectDTO.getId());
        return ResultTool.create(b);
    }
}

