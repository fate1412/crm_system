package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.insert.StockListProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.StockListProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.StockListProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IStockListProductService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = stockListProductService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 10 : pageSize;
        MyPage page = stockListProductService.listByPage(thisPage, pageSize,null);
        TableResultData tableResultData = stockListProductService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('StockListProduct_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody StockListProductInsertDTO stockListProductInsertDTO) {
        return stockListProductService.addDTO(stockListProductInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoListById = stockListProductService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = stockListProductService.getColumns();
        tableResultData.setTableDataList(dtoListById);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('StockListProduct_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody StockListProductUpdateDTO stockListProductUpdateDTO) {
        return stockListProductService.updateByDTO(stockListProductUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('StockListProduct_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody StockListProductSelectDTO stockListProductSelectDTO) {
        boolean b = stockListProductService.removeById(stockListProductSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = stockListProductService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

