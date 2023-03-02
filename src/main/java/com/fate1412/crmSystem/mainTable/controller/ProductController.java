package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.dto.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.ProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IProductService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 产品 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;
    
    @PreAuthorize("permitAll()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        List<TableColumn> tableColumns = TableResultData.tableColumnList(ProductSelectDTO.class);
        return ResultTool.success(tableColumns);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
        MyPage page = productService.listByPage(thisPage, pageSize);
        TableResultData tableResultData = TableResultData.createTableResultData(page.getRecords(), ProductSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.add(productUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = productService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = TableResultData.createTableResultData(dtoListById, ProductSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.updateById(productUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody ProductSelectDTO productSelectDTO) {
        boolean b = productService.removeById(productSelectDTO.getId());
        return ResultTool.create(b);
    }
}

