package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IInvoiceService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 发货单 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private IInvoiceService invoiceService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = invoiceService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 10 : pageSize;
        MyPage page = invoiceService.listByPage(thisPage, pageSize,null);
        TableResultData tableResultData = invoiceService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody InvoiceSelectDTO invoiceSelectDTO) {
        return invoiceService.add(invoiceSelectDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> invoiceSelectDTOList = invoiceService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = invoiceService.getColumns();
        tableResultData.setTableDataList(invoiceSelectDTOList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody InvoiceUpdateDTO invoiceUpdateDTO) {
        return invoiceService.updateById(invoiceUpdateDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody InvoiceSelectDTO invoiceSelectDTO) {
        boolean b = invoiceService.removeById(invoiceSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = invoiceService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

