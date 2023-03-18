package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.mainTable.dto.child.SalesOrderChild;
import com.fate1412.crmSystem.mainTable.dto.child.StockListChild;
import com.fate1412.crmSystem.mainTable.dto.insert.StockListInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.*;
import com.fate1412.crmSystem.mainTable.dto.update.StockListUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IStockListProductService;
import com.fate1412.crmSystem.mainTable.service.IStockListService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 备货单 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/stockList")
public class StockListController {
    @Autowired
    private IStockListService stockListService;
    @Autowired
    private IStockListProductService stockListProductService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = stockListService.getColumns();
        tableResultData.setChild(stockListProductService.getColumns(new StockListChild()));
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<StockListSelectDTO> selectPage) {
        MyPage page = stockListService.listByPage(selectPage);
        TableResultData tableResultData = stockListService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('StockList_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody StockListInsertDTO stockListInsertDTO) {
        return stockListService.addDTO(stockListInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = stockListService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = stockListService.getColumns();
        tableResultData.setTableDataList(dtoList);
        if (MyCollections.isEmpty(dtoList)) {
            tableResultData.setChild(new TableResultData());
        } else {
            StockListSelectDTO dto = (StockListSelectDTO) dtoList.get(0);
            List<StockListProductSelectDTO> stockListProductSelectDTOList = stockListProductService.getDTOByStockListId(dto.getId());
            List<StockListChild> childList = MyCollections.copyListProperties(stockListProductSelectDTOList, StockListChild::new);
            TableResultData child = stockListProductService.getColumns(new StockListChild());
            if(!MyCollections.isEmpty(childList)) {
                child.setTableDataList(childList);
            }
            tableResultData.setChild(child);
        }
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('StockList_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody StockListUpdateDTO stockListUpdateDTO) {
        return stockListService.updateByDTO(stockListUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('StockList_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody StockListSelectDTO stockListSelectDTO) {
        boolean b = stockListService.delById(stockListSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = stockListService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

