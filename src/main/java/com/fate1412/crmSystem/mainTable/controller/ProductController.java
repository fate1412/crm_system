package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.insert.ProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.ProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IProductService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = productService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
        MyPage page = productService.listByPage(thisPage, pageSize,null);
        TableResultData tableResultData = productService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody ProductInsertDTO productInsertDTO) {
        return productService.add(productInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = productService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = productService.getColumns();
        tableResultData.setTableDataList(dtoListById);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.updateById(productUpdateDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody ProductSelectDTO productSelectDTO) {
        boolean b = productService.removeById(productSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = productService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

