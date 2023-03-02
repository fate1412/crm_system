package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceUpdateDTO;
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
    
    @PreAuthorize("permitAll()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        List<TableColumn> tableColumns = TableResultData.tableColumnList(InvoiceSelectDTO.class);
        return ResultTool.success(tableColumns);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
        MyPage page = invoiceService.listByPage(thisPage, pageSize);
        TableResultData tableResultData = TableResultData.createTableResultData(page.getRecords(), InvoiceSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody InvoiceSelectDTO invoiceSelectDTO) {
        return invoiceService.add(invoiceSelectDTO);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> invoiceSelectDTOList = invoiceService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = TableResultData.createTableResultData(invoiceSelectDTOList, InvoiceSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody InvoiceUpdateDTO invoiceUpdateDTO) {
        return invoiceService.updateById(invoiceUpdateDTO);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody InvoiceSelectDTO invoiceSelectDTO) {
        boolean b = invoiceService.removeById(invoiceSelectDTO.getId());
        return ResultTool.create(b);
    }
}

